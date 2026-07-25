#include "rstack.h"

#include <ctype.h>
#include <errno.h>
#include <inttypes.h>
#include <stdio.h>
#include <stdlib.h>

typedef enum { ITEM_UINT64, ITEM_RSTACK } item_type;

typedef struct {
	item_type type;
	union {
		uint64_t u64;
		rstack_t *rstack_ptr;
	} value;
} item;

typedef struct stack {
	size_t capacity, count;
	item *data;
} stack;

struct rstack {
	stack *stack_ptr;
	bool is_reachable;
	bool visited;
	rstack_t *previous, *next;
};

static size_t total_stacks = 0;
static size_t total_edges = 0;
static size_t gc_threshold = 64;
static size_t stacks_with_outside_refs = 0;
static rstack_t *tail = nullptr;

void gc_mark(rstack_t *rs) {
	if (rs == nullptr || rs->visited) {
		return;
	}
	rs->visited = true;
	for (size_t i = 0; i < rs->stack_ptr->count; i++) {
		if ((rs->stack_ptr->data)[i].type == ITEM_RSTACK) {
			gc_mark((rs->stack_ptr->data)[i].value.rstack_ptr);
		}
	}
}

void gc_sweep() {
	rstack_t *curr = tail;
	while (curr != nullptr) {
		rstack_t *prev = curr->previous;
		if (!curr->visited) {
			if (curr->next != nullptr) {
				curr->next->previous = curr->previous;
			}
			if (curr->previous != nullptr) {
				curr->previous->next = curr->next;
			}
			if (curr == tail) {
				tail = curr->previous;
			}
			total_stacks--;
			for (size_t i = 0; i < curr->stack_ptr->count; i++) {
				if ((curr->stack_ptr->data)[i].type == ITEM_RSTACK) {
					total_edges--;
				}
			}
			free(curr->stack_ptr->data);
			free(curr->stack_ptr);
			free(curr);
		}
		else {
			curr->visited = false;
		}
		curr = prev;
	}
}

void gc_collect() {
	rstack_t *last = tail;
	while (last != nullptr) {
		if (last->is_reachable) {
			gc_mark(last);
		}
		last = last->previous;
	}
	gc_sweep();
}

rstack_t *rstack_new() {
	stack *s = malloc(sizeof(stack));
	if (s == nullptr) {
		errno = ENOMEM;
		return nullptr;
	}
	rstack_t *rs = malloc(sizeof(rstack_t));
	if (rs == nullptr) {
		errno = ENOMEM;
		free(s);
		return nullptr;
	}
	s->capacity = 1;
	s->count = 0;
	s->data = malloc(sizeof(item));
	if (s->data == nullptr) {
		errno = ENOMEM;
		free(s);
		free(rs);
		return nullptr;
	}
	rs->stack_ptr = s;
	rs->is_reachable = true;
	rs->visited = false;
	rs->previous = tail;
	if (tail != nullptr) {
		tail->next = rs;
	}
	rs->next = nullptr;
	tail = rs;
	total_stacks++;
	stacks_with_outside_refs++;
	if ((total_stacks + total_edges) > gc_threshold) {
		gc_collect();
		// Amortyzuje koszt garbage collectora.
		gc_threshold = (total_stacks + total_edges) * 2 + 64;
	}
	return rs;
}

void rstack_delete(rstack_t *rs) {
	if (rs == nullptr) {
		return;
	}
	rs->is_reachable = false;
	stacks_with_outside_refs--;
	if (stacks_with_outside_refs == 0) {
		gc_collect();
	}
}

int rstack_push_value(rstack_t *rs, uint64_t value) {
	if (rs == nullptr) {
		errno = EINVAL;
		return -1;
	}
	if (rs->stack_ptr->capacity <= rs->stack_ptr->count) {
		rs->stack_ptr->capacity *= 2;
		item *tmp;
		tmp = realloc(rs->stack_ptr->data,
		              rs->stack_ptr->capacity * sizeof(item));
		if (tmp == nullptr) {
			rs->stack_ptr->capacity /= 2;
			errno = ENOMEM;
			return -1;
		}
		rs->stack_ptr->data = tmp;
	}
	rs->stack_ptr->data[rs->stack_ptr->count].type = ITEM_UINT64;
	rs->stack_ptr->data[rs->stack_ptr->count].value.u64 = value;
	rs->stack_ptr->count++;
	return 0;
}

int rstack_push_rstack(rstack_t *rs1, rstack_t *rs2) {
	if (rs1 == nullptr || rs2 == nullptr) {
		errno = EINVAL;
		return -1;
	}
	if (rs1->stack_ptr->capacity <= rs1->stack_ptr->count) {
		rs1->stack_ptr->capacity *= 2;
		item *tmp;
		tmp = realloc(rs1->stack_ptr->data,
		              rs1->stack_ptr->capacity * sizeof(item));
		if (tmp == nullptr) {
			rs1->stack_ptr->capacity /= 2;
			errno = ENOMEM;
			return -1;
		}
		rs1->stack_ptr->data = tmp;
	}
	rs1->stack_ptr->data[rs1->stack_ptr->count].type = ITEM_RSTACK;
	rs1->stack_ptr->data[rs1->stack_ptr->count].value.rstack_ptr = rs2;
	rs1->stack_ptr->count++;
	total_edges++;
	return 0;
}

void rstack_pop(rstack_t *rs) {
	if (rs == nullptr || rs->stack_ptr->count == 0) {
		errno = EINVAL;
		return;
	}
	rs->stack_ptr->count--;
	if ((rs->stack_ptr->data)[rs->stack_ptr->count].type == ITEM_RSTACK) {
		total_edges--;
	}
	if (4 * rs->stack_ptr->count <= rs->stack_ptr->capacity &&
	    rs->stack_ptr->capacity > 1) {
		rs->stack_ptr->capacity /= 2;
		item *tmp;
		tmp = realloc(rs->stack_ptr->data,
		              rs->stack_ptr->capacity * sizeof(item));
		if (tmp == nullptr) {
			rs->stack_ptr->capacity *= 2;
			return;
		}
		rs->stack_ptr->data = tmp;
		return;
	}
}

void clear_visited(rstack_t *rs) {
	if (rs == nullptr || !rs->visited) {
		return;
	}
	rs->visited = false;
	for (size_t i = 0; i < rs->stack_ptr->count; i++) {
		if ((rs->stack_ptr->data)[i].type == ITEM_RSTACK) {
			clear_visited((rs->stack_ptr->data)[i].value.rstack_ptr);
		}
	}
}

bool rstack_empty_recursive(rstack_t *rs) {
	rs->visited = true;
	for (size_t i = 0; i < rs->stack_ptr->count; i++) {
		if ((rs->stack_ptr->data)[i].type == ITEM_UINT64) {
			return false;
		}
		else if ((!(rs->stack_ptr->data)[i].value.rstack_ptr->visited) &&
		         !rstack_empty_recursive(
		             (rs->stack_ptr->data)[i].value.rstack_ptr)) {
			return false;
		}
	}
	return true;
}

bool rstack_empty(rstack_t *rs) {
	if (rs == nullptr) {
		return true;
	}
	bool empty = rstack_empty_recursive(rs);
	clear_visited(rs);
	return empty;
}

result_t rstack_front_recursive(rstack_t *rs) {
	result_t ans;
	ans.flag = false;
	rs->visited = true;
	size_t i = rs->stack_ptr->count;
	while (!ans.flag && i > 0) {
		i--;
		if ((rs->stack_ptr->data)[i].type == ITEM_UINT64) {
			ans.flag = true;
			ans.value = (rs->stack_ptr->data)[i].value.u64;
		}
		else if ((!(rs->stack_ptr->data)[i].value.rstack_ptr->visited)) {
			ans = rstack_front_recursive(
			    (rs->stack_ptr->data)[i].value.rstack_ptr);
		}
	}
	return ans;
}

result_t rstack_front(rstack_t *rs) {
	if (rs == nullptr) {
		result_t ans;
		ans.flag = false;
		return ans;
	}
	result_t ans = rstack_front_recursive(rs);
	clear_visited(rs);
	return ans;
}

rstack_t *rstack_read(char const *path) {
	if (path == nullptr) {
		errno = EINVAL;
		return nullptr;
	}
	rstack_t *rs = rstack_new();
	if (rs == nullptr) {
		errno = ENOMEM;
		return nullptr;
	}
	FILE *source = fopen(path, "r");
	if (source == nullptr) {
		rstack_delete(rs);
		return nullptr;
	}

	bool currently_reading_number = false;
	uint64_t number = 0;
	int c;

	while ((c = fgetc(source)) != EOF) {
		if (c >= '0' && c <= '9') {
			currently_reading_number = true;
			uint64_t digit = c - '0';
			if (number > (UINT64_MAX - digit) / 10) {
				errno = ERANGE;
				rstack_delete(rs);
				fclose(source);
				return nullptr;
			}
			number = number * 10 + digit;
		}
		else {
			if (currently_reading_number) {
				if (rstack_push_value(rs, number) == -1) {
					rstack_delete(rs);
					fclose(source);
					return nullptr;
				}
				number = 0;
				currently_reading_number = false;
			}
			if (isspace(c)) {
				continue;
			}
			errno = EINVAL;
			rstack_delete(rs);
			fclose(source);
			return nullptr;
		}
	}
	if (currently_reading_number) {
		if (rstack_push_value(rs, number) == -1) {
			rstack_delete(rs);
			fclose(source);
			return nullptr;
		}
	}

	fclose(source);
	return rs;
}

// Przekazywanie wskaźnika do pliku pozwala uniknąć jego wielokrotnego
// otwierania.
static int rstack_write_helper(rstack_t *rs, FILE *destination) {
	rs->visited = true;
	for (size_t i = 0; i < rs->stack_ptr->count; i++) {
		if ((rs->stack_ptr->data[i]).type == ITEM_UINT64) {
			if (fprintf(destination, "%" PRIu64 "\n",
			            (rs->stack_ptr->data[i]).value.u64) < 0) {
				rs->visited = false;
				return -1;
			}
		}
		else if (!(rs->stack_ptr->data[i]).value.rstack_ptr->visited) {
			int status = rstack_write_helper(
			    (rs->stack_ptr->data[i]).value.rstack_ptr, destination);
			if (status == -1) {
				rs->visited = false;
				return -1;
			}
			else if (status == -2) {
				rs->visited = false;
				return -2;
			}
		}
		else {
			// Wystąpił cykl.
			rs->visited = false;
			return -2;
		}
	}
	rs->visited = false;
	return 0;
}

int rstack_write(char const *path, rstack_t *rs) {
	if (rs == nullptr) {
		errno = EINVAL;
		return -1;
	}
	if (path == nullptr) {
		errno = EINVAL;
		return -1;
	}
	FILE *destination = fopen(path, "w+");
	if (destination == nullptr) {
		return -1;
	}
	int result = rstack_write_helper(rs, destination);
	fclose(destination);
	if (result == -2)
		result = 0;
	return result;
}
