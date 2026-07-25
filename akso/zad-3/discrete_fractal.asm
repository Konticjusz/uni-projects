global _start
SYS_EXIT equ 60
SYS_MUNMAP equ 11
SYS_MMAP equ 9
SYS_READ equ 0
SYS_WRITE equ 1
SYS_MREMAP equ 25

section .data
newline_char: db 10

section .bss
; Liczba iteracji do wykonania
iterations resq 1

; Adres bufora na dane wejściowe
input_buffer resq 1
; Rozmiar bufora
input_buffer_size resq 1
; Ilość odczytanych bajtów
input_size resq 1

; Wskaźnik do ciągu początkowego
init_str: resq 1
; Długość ciągu początkowego
init_len: resq 1

; Tablica wskaźników na reguły
rule_ptr: resq 256
; Tablica długości reguł
rule_len: resq 256

; Bufor na aktualny ciąg
str1 resq 1
; Pojemność bufora str1
str1_cap resq 1
; Długość aktualnego ciągu
str1_len resq 1

; Bufor na nowy ciąg
str2 resq 1
; Pojemność bufora str2
str2_cap resq 1
; Długość nowego ciągu
str2_len resq 1

section .text

_start:
; Sprawdź czy liczba argumentów wynosi 2.
    cmp qword [rsp], 2
; Jeśli nie, przejdź do obsługi błędu.
    jne exit_error
; Ustaw rsi na wskażnik do liczby iteracji.
    mov rsi, [rsp+16]
    call parse_uint32
; Zapisz liczbę iteracji.
    mov [rel iterations], rax
; Wczytaj dane wejściowe do bufora.
    call read_input
; Przetwórz dane wejściowe, wypełniając struktury danych.
    call parse_input
; Przygotuj bufor str1 z ciągiem początkowym.
    call setup_buffers
; Wykonaj iteracje generowania ciągu.
    call iterate
; Zapisz wynik do standardowego wyjścia i zakończ program.
    call write_output

exit_success:
; Zwolnij pamięć, jeśli została przydzielona.
    call free_memory
    mov rax, SYS_EXIT
; Kod wyjścia 0
    xor rdi, rdi
    syscall

exit_error:
; Zwolnij pamięć, jeśli została przydzielona.
    call free_memory
    mov rax, SYS_EXIT
; Kod wyjścia 1
    mov rdi, 1
    syscall

; Funkcja zwalnia przydzieloną pamięć dla bufora wejściowego, str1 i str2.
free_memory:
    mov rdi, [rel input_buffer]
    test rdi, rdi
; Jeśli bufor wejściowy nie został przydzielony, przejdź do zwalniania str1.
    jz .free_str1
    mov rsi, [rel input_buffer_size]
    mov rax, SYS_MUNMAP
; Zwolnij bufor wejściowy.
    syscall
.free_str1:
    mov rdi, [rel str1]
    test rdi, rdi
; Jeśli bufor str1 nie został przydzielony, przejdź do zwalniania str2.
    jz .free_str2
    mov rsi, [rel str1_cap]
; Zwolnij bufor str1.
    mov rax, SYS_MUNMAP
    syscall
.free_str2:
    mov rdi, [rel str2]
    test rdi, rdi
    jz .free_done
    mov rsi, [rel str2_cap]
; Zwolnij bufor str2.
    mov rax, SYS_MUNMAP
    syscall
.free_done:
    ret

; Funkcja parsuje argument z liczbą iteracji jako uint32.
; rsi - wskaźnik do stringa z liczbą
; rax - wynik parsowania (uint32_t)
; rdx - tymczasowa zmienna na aktualną cyfrę
parse_uint32:
    xor rax, rax              
    xor rcx, rcx              
.parse_loop:
; Pobierz aktualny znak.
    movzx edx, byte [rsi+rcx]
; Sprawdź czy to koniec stringa.
    test dl, dl
; Jeśli tak, zakończ parsowanie.
    je .parse_done
; Sprawdź czy to cyfra.
    cmp dl, '0'
; Jeśli nie, to błąd.
    jb exit_error
; Sprawdź czy to cyfra.
    cmp dl, '9'
; Jeśli nie, to błąd.
    ja exit_error
; Zamień znak na wartość liczbową.
    sub rdx, '0'
; Pomnóż aktualny wynik przez 10
    imul rax, rax, 10
    add rax, rdx                        
    mov rdx, 0xFFFFFFFF
; Sprawdź czy wynik mieści się w uint32_t.
    cmp rax, rdx
    ja exit_error
    inc rcx
    jmp .parse_loop
.parse_done:
    test rcx, rcx
; Jeśli nie było żadnych cyfr, to błąd.
    jz exit_error
    ret

; Funkcja wczytuje dane wejściowe do bufora.
read_input:
; Rozmiar bufora wejściowego
    mov rdi, 4096
    call alloc_mmap
; Zapisz adres bufora.
    mov [rel input_buffer], rax
; Zapisz rozmiar bufora.
    mov qword [rel input_buffer_size], 4096
; Zainicjuj ilość odczytanych bajtów.
    mov qword [rel input_size], 0
.read_loop:
    mov rax, SYS_READ
; stdin
    mov rdi, 0
    mov rsi, [rel input_buffer]
; Ustaw wskaźnik na koniec już odczytanych danych.
    add rsi, [rel input_size]
; Maksymalna liczba bajtów do odczytania.
    mov rdx, [rel input_buffer_size]
; Pozostała ilość miejsca w buforze.
    sub rdx, [rel input_size]
    syscall
; Sprawdź czy osiągnięto EOF.
    cmp rax, 0
    je .read_done
; Sprawdź czy wystąpił błąd.
    cmp rax, -4095
    ja exit_error
; Zaktualizuj ilość odczytanych bajtów.
    add [rel input_size], rax
    mov rbx, [rel input_size]
; Sprawdź czy bufor jest pełny.
    cmp rbx, [rel input_buffer_size]
; Jeśli nie, kontynuuj odczyt.
    jb .read_loop
    mov rdi, [rel input_buffer]
    mov rsi, [rel input_buffer_size]
    mov rdx, rsi
; Podwój rozmiar bufora.
    shl rdx, 1
; MREMAP_MAYMOVE
    mov r10, 1
    mov rax, SYS_MREMAP
; Zapamiętaj nowy rozmiar bufora.
    push rdx
    syscall
; Przywróć nowy rozmiar bufora.
    pop rdx
; Sprawdź czy mremap zwróci błąd.
    cmp rax, -4095
    ja exit_error
; Zaktualizuj adres bufora.
    mov [rel input_buffer], rax
; Zaktualizuj rozmiar.
    mov [rel input_buffer_size], rdx
    jmp .read_loop
.read_done:
    ret
    
parse_input:
    mov rsi, [rel input_buffer]
    mov rdi, rsi                             
; Ustaw rdi na koniec danych wejściowych.
    add rdi, [rel input_size]
    cmp rsi, rdi
; Jeśli bufor jest pusty, to błąd.
    je exit_error
    mov rdx, rsi
.find_first_newline_loop:
; Sprawdź czy nie przekroczyliśmy końca bufora.
    cmp rdx, rdi
; Jeśli tak, to błąd.
    je exit_error
; Pobierz aktualny znak.
    mov al, [rdx]
; Sprawdź czy to znak nowej linii.
    cmp al, 10
; Jeśli tak, to znaleźliśmy koniec ciągu początkowego.
    je .found_init
; Sprawdź czy znak jest poprawny (33-126).
    cmp al, 33
    jb exit_error
    cmp al, 126
    ja exit_error
    inc rdx
    jmp .find_first_newline_loop
.found_init:
    mov rax, rdx
; Oblicz długość ciągu początkowego
    sub rax, rsi
; Zapisz wskaźnik do ciągu początkowego.
    mov [rel init_str], rsi
; Zapisz długość ciągu początkowego.
    mov [rel init_len], rax
; Przesuń rdx za znak nowej linii.
    inc rdx
.parse_rules_loop:
; Sprawdź czy nie przekroczyliśmy końca bufora.
    cmp rdx, rdi
; Jeśli tak, zakończ parsowanie.
    je .parse_done
; Pobierz znak X.
    movzx r12d, byte [rdx]
; Sprawdź czy znak X jest poprawny (33-126).
    cmp r12d, 33
    jb exit_error
    cmp r12d, 126
    ja exit_error
; Przesuń rdx na początek ciągu zastępującego.
    inc rdx
; Załaduj adres tablicy rule_ptr.
    lea rbx, [rel rule_ptr]
; Pobierz wskaźnik do reguły dla znaku.
    mov r10, [rbx + r12*8]
; Sprawdź czy już istnieje reguła dla tego znaku.
    test r10, r10
; Jeśli tak, to błąd (powtarzający się znak).
    jnz exit_error
; Zapisz wskaźnik do reguły dla znaku.
    mov [rbx + r12*8], rdx
; Zapisz początek ciągu zastępującego w r8.
    mov r8, rdx
.find_rule_newline_loop:
; Jeśli r8 przekroczyło koniec bufora, to błąd.
    cmp r8, rdi
    je exit_error
; Pobierz aktualny znak.
    mov al, [r8]
; Sprawdź czy to znak nowej linii.
    cmp al, 10
    je .found_rule
; Sprawdź czy znak jest poprawny (33-126).
    cmp al, 33
    jb exit_error
    cmp al, 126
    ja exit_error
; Przesuń r8 na kolejny znak ciągu zastępującego.
    inc r8
    jmp .find_rule_newline_loop
.found_rule:
    mov rax, r8
; Oblicz długość reguły.
    sub rax, rdx
; Załaduj adres tablicy rule_len.
    lea rbx, [rel rule_len]
; Zapisz długość reguły dla aktualnego znaku.
    mov [rbx + r12*8], rax
; Przesuń rdx za znak nowej linii, przygotowując się do kolejnej reguły.
    lea rdx, [r8 + 1]
    jmp .parse_rules_loop
.parse_done:
    ret

; Funkcja przydziela pamięć za pomocą mmap.
; rdi - rozmiar pamięci do przydzielenia
; rax - adres przydzielonej pamięci lub błąd
alloc_mmap:
    mov rax, SYS_MMAP
    mov rsi, rdi                             
; Adres 0 - pozwalamy systemowi wybrać.
    xor rdi, rdi
; PROT_READ | PROT_WRITE
    mov rdx, 3
; MAP_PRIVATE | MAP_ANONYMOUS
    mov r10, 0x22
; fd = -1
    mov r8, -1
; offset = 0
    xor r9, r9
    syscall
; Sprawdź czy mmap zwrócił błąd (adresy ujemne są błędami).
    cmp rax, -4095
    ja exit_error
    ret

; Funkcja przygotowuje bufor do przechowywania ciągów.
setup_buffers:
    mov rdi, [rel init_len]
; Przydziel dodatkowo 4KB na wypadek pustego ciągu.
    add rdi, 4096
; Zaokrąglij do pełnego rozmiaru strony.
    and rdi, -4096
; Zapisz pojemność bufora str1.
    mov [rel str1_cap], rdi
    call alloc_mmap
; Zapisz adres bufora str1.
    mov [rel str1], rax
; Zapisz długość ciągu początkowego do rax.
    mov rax, [rel init_len]
; Zapisz długość ciągu początkowego do str1_len.
    mov [rel str1_len], rax
; Skopiuj ciąg początkowy do bufora str1.
; Źródło - ciąg początkowy
    mov rsi, [rel init_str]
; Cel - bufor str1
    mov rdi, [rel str1]
; Długość do skopiowania.
    mov rcx, [rel init_len]
    test rcx, rcx
; Jeśli długość jest 0, przejdź do przygotowania str2.
    jz .alloc_str2
    rep movsb
.alloc_str2:
; Przygotuj bufor str2 o początkowej pojemności równej 1 stronie.
    mov rdi, 4096                            
; Zapisz pojemność bufora str2.
    mov [rel str2_cap], rdi
    call alloc_mmap
; Zapisz adres bufora str2.
    mov [rel str2], rax
    ret

; Funkcja wykonuje kolejne iteracje generacji fraktala.
iterate:
; Załaduj liczbę iteracji do r13.
    mov r13, [rel iterations]
    test r13, r13
; Jeśli liczba iteracji jest 0, zakończ iterowanie.
    jz .exit_iterate
.iterate_loop:
; r12 - długość nowego ciągu
    xor r12, r12
; Załaduj adres aktualnego ciągu.
    mov rsi, [rel str1]
; Załaduj długość aktualnego ciągu.
    mov rdi, [rel str1_len]
    test rdi, rdi
; Jeśli aktualny ciąg jest pusty, zakończ iterowanie.
    jz .exit_iterate
; Ustaw rdi na koniec aktualnego ciągu.
    add rdi, rsi
; Załaduj adres tablicy rule_len.
    lea rbx, [rel rule_len]
.precalculate_length_loop:
; Sprawdź czy przetworzyliśmy cały aktualny ciąg.
    cmp rsi, rdi
; Jeśli tak, przejdź do sprawdzania pojemności bufora str2.
    je .check_capacity
; Pobierz aktualny znak z aktualnego ciągu.
    movzx eax, byte [rsi]
; Załaduj adres tablicy rule_ptr.
    lea rbx, [rel rule_ptr]
; Pobierz wskaźnik do reguły dla znaku al (0 jeśli brak reguły).
    mov rbx, [rbx + rax*8]
    test rbx, rbx
    jnz .has_rule
; Brak reguły, więc znak zostanie skopiowany.
    inc r12
; Przejdź do kolejnego znaku w aktualnym ciągu.
    inc rsi
    jmp .precalculate_length_loop
.has_rule:
; Załaduj adres tablicy rule_len.
    lea rbx, [rel rule_len]
; Pobierz długość reguły dla znaku al (0 jeśli brak reguły).
    mov r8, [rbx + rax*8]
; Dodaj długość reguły do całkowitej długości nowego ciągu.
    add r12, r8
; Przejdź do kolejnego znaku w aktualnym ciągu.
    inc rsi
    jmp .precalculate_length_loop
.check_capacity:
; Załaduj pojemność bufora str2.
    mov rdi, [rel str2_cap]
; Sprawdź czy nowy ciąg mieści się w buforze str2.
    cmp r12, rdi
; Jeśli tak to przejdź do generowania nowego ciągu.
    jbe .generate_new_string

; Jeśli nie, to musimy powiększyć bufor str2.
; Nowy rozmiar bufora str2 (długość nowego ciągu)
    mov rcx, r12
; Zaokrąglij do pełnych stron (4096 bajtów).
    add rcx, 4095
    and rcx, -4096
; Stary adres bufora str2
    mov rdi, [rel str2]
; Stary rozmiar bufora str2
    mov rsi, [rel str2_cap]
; Nowy rozmiar bufora str2
    mov rdx, rcx
    mov r10, 1
    mov rax, SYS_MREMAP
; Nowy rozmiar
    push rcx
    syscall
; Przywróć nowy rozmiar bufora str2.
    pop rcx
; Sprawdź czy mremap zwrócił błąd.
    cmp rax, -4095
    ja exit_error
; Zaktualizuj adres bufora str.
    mov [rel str2], rax
; Zaktualizuj rozmiar bufora str2.
    mov [rel str2_cap], rcx
.generate_new_string:
; Załaduj adres aktualnego ciągu.
    mov rsi, [rel str1]
    mov r8, rsi
; Ustaw r8 na koniec aktualnego ciągu.
    add r8, [rel str1_len]
; Załaduj adres bufora str2.
    mov rdi, [rel str2]
.generate_loop:
; Sprawdź czy przetworzyliśmy cały aktualny ciąg.
    cmp rsi, r8
    je .finish_generation                    
; Pobierz aktualny znak z aktualnego ciągu.
    movzx eax, byte [rsi]
; Przejdź do kolejnego znaku w aktualnym ciągu.
    inc rsi
    lea rbx, [rel rule_ptr]                  
; Pobierz wskaźnik do reguły dla znaku al.
    mov rdx, [rbx + rax*8]
    test rdx, rdx                       
; Jeśli brak reguły, skopiuj znak do nowego ciągu.
    jz .copy_char
; Załaduj adres tablicy rule_len.
    lea r10, [rel rule_len]
; Pobierz długość reguły dla znaku al.
    mov r11, [r10 + rax*8]
    test r11, r11
; Jeśli długość reguły jest 0, przejdź do kolejnego znaku.
    jz .generate_loop
; Zachowaj rsi na stosie, ponieważ rep movsb może zmienić rsi.
    push rsi
; Ustaw rsi na początek ciągu zastępującego.
    mov rsi, rdx
; Ustaw rcx na długość ciągu zastępującego.
    mov rcx, r11
; Skopiuj ciąg zastępujący do nowego ciągu.
    rep movsb
; Przywróć rsi z stosu.
    pop rsi
; Przejdź do kolejnego znaku w aktualnym ciągu.
    jmp .generate_loop
.copy_char:
; Skopiuj znak do nowego ciągu.
    mov byte [rdi], al
; Przesuń wskaźnik docelowy na kolejny znak w nowym ciągu.
    inc rdi
    jmp .generate_loop
.finish_generation:
; Zamień str1 z str2 (nowy ciąg staje się aktualnym ciągiem).
    mov rax, rdi
; Oblicz długość nowego ciągu.
    sub rax, [rel str2]
; Zapisz długość nowego ciągu do str2_len.
    mov [rel str2_len], rax

; Podmiana wskaźników i rozmiarów str1 i str2.
    mov rax, [rel str1]
    mov rbx, [rel str2] 
    mov [rel str1], rbx
    mov [rel str2], rax
    mov rax, [rel str1_len]
    mov rbx, [rel str2_len]
    mov [rel str1_len], rbx
    mov [rel str2_len], rax
    mov rax, [rel str1_cap]
    mov rbx, [rel str2_cap]
    mov [rel str1_cap], rbx
    mov [rel str2_cap], rax

; Zmniejsz licznik iteracji.
    dec r13
    jnz .iterate_loop                   
.exit_iterate:
    ret

; Funkcja wypisuje str1 na standardowe wyjście, a następnie znak nowej linii.
write_output:
; Załaduj adres aktualnego ciągu.
    mov rsi, [rel str1]
; Załaduj długość aktualnego.
    mov rdx, [rel str1_len]
; Zapisz cały ciąg do standardowego wyjścia.
    call write_loop
; Załaduj adres znaku nowej linii.
    lea rsi, [rel newline_char]
    mov rdx, 1
; Zapisz znak nowej linii.
    call write_loop
    ret

; Pętla do wypisywania danych na standardowe wyjście.
; rsi - wskaźnik do danych do wypisania
; rdx - liczba bajtów do wypisania
write_loop:
    cmp rdx, 0                               
    je .done_write
    mov rax, SYS_WRITE
; stdout
    mov rdi, 1
    syscall
    test rax, rax
; Jeśli wystąpił błąd podczas zapisu, zakończ z błędem.
    js exit_error
; Przesuń wskaźnik na kolejny fragment do wypisania.
    add rsi, rax
; Zmniejsz pozostałą długość do wypisania.
    sub rdx, rax
    jmp write_loop
.done_write:
    ret
    
    
