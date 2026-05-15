/* FOUR LETTER WORDS */
/* This program identifies all four letter words */
/*   in the input and places them into an output list. */

four_letter_words = ''

say "Enter a word:"
parse pull wordin .

do while wordin \= ''
    if length(wordin) = 4 then
        four_letter_words = four_letter_words wordin

    say "Enter a word:"
    parse pull wordin .
end

say 'Four letter words:' four_letter_words
