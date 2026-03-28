
# SHOC-Shoresh Compiler- a biblical hebrew language compiler



![shoresh](https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExZW85OHE4djV1ZzF2ZDVpZHBvNDE2MWlzejZhbHZqNmZxN2k0MnBqeSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/2qqAyB5sY1PKxoDTTc/giphy.gif)


## Installation

Install shoc with npm
(only for linux)
```bash
  sudo npm install -g shoc-shoresh-compiler
```

i also recommend installing the complementary notepad++  [UDL pack](https://github.com/acetim/shoresh_NPP_config) to edit .sho files and code with syntax highlighting and logical mirroring when using RTL
    
## Usage

this will compile your shoresh source code into a linux native asm(x86_64) file 


```bash
shoc <output .s file> <input .sho file>
```
to assemble and link this file to an executable:
```bash
gcc -o <your executable name> <the compiled .s file>
```

use the -no-shabat-chk flag to bypass shabat check func (prevents you from compiling during the sabbath) in the compiler & to not add the function that runs at the start of each program (thats compiled with shoc) that prevents the user from running it in sabbath 
```bash
shoc -no-shabat-chk <output .s file> <input .sho file>
```

use the -arm64 flag to cross-compile to AArch64:
```bash
shoc -arm64 <output .s file> <input .sho file>
```


## how to write in shoresh?

i will use this fibonnacci program as an example:

this is the [fibbonacci func](https://github.com/acetim/fib-in-Shoresh/blob/main/fib.sho):
![img](https://i.postimg.cc/D04F5bfT/Screenshot-2026-03-16-004734.png)

you should always start your program with a בסד statement

your entry point function should be called בראשית ,take no args and return void(תהו_ובהו)









## 
FUNCTION DECLERATION:


![func](https://i.postimg.cc/651bt1hP/Screenshot-2026-03-16-005809.png)


 use the ויברא keyword to start func decleration then specify return value (currently shoresh has only int (שלם) and void(תהו_ובהו) types )
then specify your func name and its argument in the parentheses 

now open a new code block using ויעש and then when you want to close it use ויתם (same goes for if and while)


to return a value (or just return from a void func) use the keyword אמן and then your return value + a semicolon (or just אמן and a semicolon if returning no value)
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


## 

FUNCTION CALLING



![func](https://i.postimg.cc/C1vSJWRJ/Screenshot-2026-03-16-214039.png)


to call a void returning func use ויקרא


to call a value returning func just call it inside an expression

make sure all arguments that are passed to the func end in a semi colon; remember - they are all individual expressions and as i mentioned-EACH INDIVIDUAL EXPRESSION SHOULD END IN A SEMICOLON


## 
OTHER FUNCTIONS


![imgf](https://i.postimg.cc/nrRKFcCD/Screenshot-2026-03-16-214912.png)


to print an expression: ויאמר_שלם

to print a string:ויאמר

to use SYSEXIT : ויהי_חושך with your exit code
## TBC!!

probably gonna do arm64 compilation and add arrays
