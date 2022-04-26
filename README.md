# Wordle 
In this assignment, I have implemented a clone for Wordle in Java using
Swing and AWT.

## How to play
It's quite simple. You have 6 guesses to figure out a 5 lettered word. 
For each guess, the game tells you if you have a letter in the correct 
spot, incorrect spot, or if the letter is not a part of the word at all.
You keep on guessing until you're either out of turns, or you figure out
the word.

## My implementation
I wanted my implementation to be as similar as possible which was hard to do
as Swing is quite old. But, I have tried my best to make it as similar as possible,
and it works quite well. I have used the same colours as in the New York Times
to make it even more identical.

The word to guess, in my implementation, comes from a list of words in the file 
called "targetWords.txt". This is created to make sure the user doesn't have
to guess archaic words and unusual spellings. 
The user can input all types of words (including unusual ones) into the
game from the "gameDictionary.txt" file which is a dictionary of 5-lettered
words.

For testing purposes, I have also outputted the word on the console in my
implementation.

While the New York Times' Wordle generates 1 new wordle each day, this 
implementation generates a new one each time it is run.