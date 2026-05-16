/* FOUR LETTER WORDS */
/* This program identifies all four letter words */
/* in the input and places them into an output list. */

SAY 'This program identifies all four letter words in the input and places them into an output list.'
CALL main
EXIT

main:
   four_letter_words = ''    /* initialize to no 4-letter words found yet */

   say "Enter a word:"
   parse pull wordin .      /* the period ensures only 1 word is read in */

   do while wordin \= ''

      if length(wordin) = 4 then
         four_letter_words = four_letter_words wordin

      say "Enter a word:"
      parse pull wordin .  /* read the next word in */

   end

   say 'Four letter words:' four_letter_words
   say 'End of the program.'
   return
