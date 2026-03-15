
# SHOC-Shoresh Compiler- a biblical hebrew language compiler

A brief description of what this project does and who it's for

![shoresh](https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExZW85OHE4djV1ZzF2ZDVpZHBvNDE2MWlzejZhbHZqNmZxN2k0MnBqeSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/2qqAyB5sY1PKxoDTTc/giphy.gif)

## Installation

Install shoc with npm
(only for linux)
```bash
  sudo npm install -g shoc-shoresh-compiler
```
    
## Usage

this will compile your shoresh source code into a linux native asm(x86_64) file 


```bash
shoc <output .s file> <input .sho file>
```
to assemble and link this file to an executable:
```bash
gcc -o <your executable name> <the compiled .s file>
```


## how to write in shoresh?

i will use this fibonnaci program as an example:

this is the fibonnaci func:
![img](https://i.postimg.cc/D04F5bfT/Screenshot-2026-03-16-004734.png)

you should always start your program with a בסד statement

your entry point function should be called בראשית ,take no args and return void(תהו_ובהו)









## 
FUNCTION DECLERATION:


![func](https://i.postimg.cc/651bt1hP/Screenshot-2026-03-16-005809.png)


 use the ויברא keyword to start func decleration then specify return value (currently shoresh has only int (שלם) and void(תהו_ובהו) types )
then specify your func name and its argument in the parentheses 

now open a new code block using ויעש and then when you want to close it use ויתם (same goes for if and while)
## 

INTEGER DECLERATION:


![int](https://i.postimg.cc/76mMXfsG/Screenshot-2026-03-16-010129.png)
you can initilize it with a number and you can leave it blank 
make sure you pay attention to the scopes!! an integer defined inside a code block (e.g if or while blocks) wouldnt be accessible out of that block!
## 
BASIC OPERATIONS:


![ops](https://i.postimg.cc/ZYXjQyYD/Screenshot-2026-03-16-010525.png)



available logic operations:  
or-או   
not-לא  
and- וגם

other available operations : + - * / = > <   
you can also use parentheses in your expressions   
!!!make sure you end EACH INDIVIDUAL EXPRESSION WITH ;!!!
## 



IF AND WHILE:


![controlflow](https://i.postimg.cc/TPrXyX72/Screenshot-2026-03-16-011308.png)



use בעוד  for while and אם_יהיה for if statements  
make sure your statement is a boolean value!


MORE COMNG SOON...