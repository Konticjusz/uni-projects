global arithmetic_sequence

section .text


arithmetic_sequence:
        mov     r9, rdi                 ; r9 = wskaźnik na A0
        mov     r11, rdx                ; r11 = Ak 
        mov     r10, r8                 ; r10 = k 
        test    r10, r10                ; sprawdzenie czy k >= 0
        jns     .k_pos                  
        neg     r10                     ; r10 = |k|
        xchg    rdi, rsi                ; zamiana wskaźnika A0 z A1
.k_pos:
                                        ; przesunięcie wskaźników za koniec, by używać indeksu ujemnego
        lea     rdi, [rdi + rcx*8]
        lea     rsi, [rsi + rcx*8]
        lea     r9,  [r9  + rcx*8]
        lea     r11, [r11 + rcx*8]
        neg     rcx                     ; rcx = -n, pozwala na użycie rcx jednocześnie do wyliczania indeksów i pętli
        mov     r8, rcx                 ; r8 = -n 
        clc                             ; wyzerowanie flagi carry

                                        ; obliczenie różnicy A1 i A0
.loop_diff:
        mov     rax, [rsi + rcx*8]      ; rax = A1[i] (A0[i] jeśli k < 0)
        sbb     rax, [rdi + rcx*8]      ; rax = D[i], nowa wartość flagi carry
        mov     [r11 + rcx*8], rax      ; D[i] trzymane w Ak[i]
        inc     rcx                     
        jnz     .loop_diff

                                        ; obliczenie znaku różnicy

        mov     rax, [rsi - 8]          ; rax = A1[n-1] (A0[n-1] jeśli k < 0)
        cqo                             ; rdx = znak z rax
        xchg    rdx, rdi                ; rdx = wskaźnik za A0, rdi = znak A1[n-1]
        mov     rax, [rdx - 8]          ; rax = A0[n-1]
        cqo                             ; rdx = znak A0[n-1]
        sbb     rdi, rdx                ; rdi = różnica znaków A1 i A0 - pożyczka

        xchg    rcx, r8                 ; rcx = -n, r8 = 0

                                        ; obliczenie finalnego Ak
.loop_final:
        mov     rax, [r11 + rcx*8]      ; rax = D[i]
        mul     r10                     ; rdx:rax = D[i] * |k|
        add     rax, r8                 ; dodanie przeniesienia z poprzedniego mnożenia (na początku zero)
        adc     rdx, 0
        add     rax, [r9 + rcx*8]       ; dodanie A0[i]
        adc     rdx, 0
        mov     [r11 + rcx*8], rax      ; zapisanie finalnego Ak[i]
        mov     r8, rdx                 ; nowe przeniesienie
        inc     rcx
        jnz     .loop_final

                                        ; obliczenie 128 starszych bitów

        mov     rax, [r9 - 8]           ; rax = A0[n-1]
        cqo                             ; rdx = znak z A0[n-1]
        mov     rcx, rdx                
                                        ; obliczenie lo
        mov     rax, rdi                ; rax = wcześniej policzona różnica znaków
        mul     r10                     ; mnożenie przez |k|
        add     rax, r8                 ; dodanie przeniesienia
        adc     rdx, 0
        add     rax, rcx                ; dodanie znaku z A0
        adc     rdx, 0
        mov     rsi,  rax               ; rsi = lo
        mov     r8, rdx                 ; nowe przeniesienie

                                        ; obliczenie podobnie hi

        mov     rax, rdi                ; rax = różnica znaków
        mul     r10
        add     rax, r8
        adc     rdx, 0
        add     rax, rcx
        adc     rdx, 0                  ; rax = hi 

        mov rdx, rax                   ; rdx = hi
        mov rax, rsi                   ; rax = lo
        ret
