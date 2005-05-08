//Troll_Attack.cpp © Stephen Fluin 2002
//Modified Dec. 14, 2002
//Programmed by Stephen Fluin and areas written by Stephen Fluin.
//---------------------------------------------------------------------------
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream.h>
#include <conio.h>

int cur_room=1;
struct char_data { //information about the character
char prompt[60];
int hit, max_hit, xp;
int objects[30];
} ch;


struct room_data { //information about the rooms
char name[40], desc[256];
int east, up, down, west, north, south, ne, sw, se, nw, amtobj, objects[32];
} room[100];

struct object_data { //information about the objects
char name[40], longd[60], shortd[40];
int hp;
} object[100];


//soon to come: information about the mobs

void look_room(int cur_room=1);
void show_prompt();
void read_command(char arg[32]);
void do_score();
int times;
int main()
{
int a,location=0;
char command[40];
strcpy(ch.prompt,"&G<&R%h&G/&R%H&G-&z%x&G> %T");
ch.hit=60;
ch.max_hit=60;
ch.xp=1;
strcpy(command,"");
#include "Rooms.txt"


textcolor(WHITE);
cprintf("Welcome to: _______ ______ _______ ___ ___ \n\r");
cprintf(" / / / __ / / __ // / / / \n\r");
cprintf(" /__ __/ / /_/ / / /_/ // / / / \n\r");
cprintf(" / / / __ / / // /____ / /____ \n\r");
cprintf(" /_/ /_/ \\_\\/______//_______//_______/ \n\r");
cprintf(" _ _______ _______ _ __ _ __ \n\r");
cprintf(" / \\ / // // \\ / _\\ / \\/ / \n\r");
cprintf(" / ^ \\ /__ __//__ __// ^ \\ | | / / \n\r");
cprintf(" / / \\ \\ / / / / / / \\ \\ | |_ / \\\n\r");
cprintf(" /_/ \\ \\ /_/ /_/ /_/ \\_\\\\__/ /__/\\___\\\n\r");
textcolor(GREEN);
cprintf(" [--Press Enter--]\n\r");
getch();
look_room(cur_room);
while(cur_room) //main loop
{ //
if(kbhit())
{
if(command[0]==' ')
show_prompt(); // prompt
a=getch();
switch (a) {
case 13: // get input
location=0;
strcpy(arg,command); // copy command into global var 'arg'
read_command(command);

break;
case 108:
command[location]='l';
cprintf("l");
location++;
break;
case 27:
strcpy(command,"quit");
break;
default:
command[location]=a;
cprintf("\n\rYour current command:%s, %d, in position %d.",command, command[location],location);
location++;
break; // use the global 'arg' to do the command
}
}
times++;
} //
textcolor(LIGHTBLUE);
cprintf("\n\r\n\r[connection closed]\n\r");
getch();
return 0;
}

void look_room(int cur_room)
{
textcolor(WHITE);
cprintf("%s\n\r",room[cur_room].name);
textcolor(YELLOW);
cprintf("%s\n\r",room[cur_room].desc);
textcolor(WHITE);
cprintf("Exits: ");
int exits=0;
if (room[cur_room].east) { cprintf("East"); exits++; }
if (room[cur_room].west) { if (exits>0) cprintf(", ");cprintf("West"); exits++; }
if (room[cur_room].south) { if (exits>0) cprintf(", ");cprintf("South"); exits++; }
if (room[cur_room].north) { if (exits>0) cprintf(", ");cprintf("North"); exits++; }
if (room[cur_room].ne) { if (exits>0) cprintf(", ");cprintf("Northeast"); exits++; }
if (room[cur_room].sw) { if (exits>0) cprintf(", ");cprintf("Southwest"); exits++; }
if (room[cur_room].nw) { if (exits>0) cprintf(", ");cprintf("Northwest"); exits++; }
if (room[cur_room].se) { if (exits>0) cprintf(", ");cprintf("SouthEast"); exits++; }
if (room[cur_room].up) { if (exits>0) cprintf(", ");cprintf("Up"); exits++; }
if (room[cur_room].down) { if (exits>0) cprintf(", ");cprintf("Down"); exits++; }
cprintf("\n\r\n\r");
}


void show_prompt()
{
int i=0, n=0;
char stat;
int num_stat;
while(ch.prompt[i])
{
if(ch.prompt[i]=='%')
{
switch(ch.prompt[i+1]) {
case 'h':
cprintf("%d",ch.hit);
break;
case 'H':
cprintf("%d",ch.max_hit);
break;
case 'x':
cprintf("%d",ch.xp);
break;
case 'T':
cprintf("%d",times);
break;
}
i+=2;
}
else if(ch.prompt[i]=='&')
{
switch(ch.prompt[i+1]) {
case 'r':
textcolor(RED);
break;
case 'R':
textcolor(LIGHTRED);
break;
case 'g':
textcolor(GREEN);
break;
case 'G':
textcolor(LIGHTGREEN);
break;
case 'z':
textcolor(LIGHTGRAY);
break;
}
i+=2;
}
else
{
cprintf("%c",ch.prompt[i]);
n++;
i++;
}
}
textcolor(WHITE);
}

void read_command(char arg[32])
{
if(!strcmpi(arg,"quit")) cur_room=0;
else if(!strcmpi(arg,"south")||!strcmpi(arg,"s")) {if(room[cur_room].south) {cur_room=room[cur_room].south;look_room(cur_room);} else cprintf("Alas, you cannot go that way.");}
else if(!strcmpi(arg,"east")||!strcmpi(arg,"e")) {if(room[cur_room].east) {cur_room=room[cur_room].east;look_room(cur_room);} else cprintf("Alas, you cannot go that way."); }
else if(!strcmpi(arg,"north")||!strcmpi(arg,"n")) {if(room[cur_room].north) {cur_room=room[cur_room].north;look_room(cur_room);} else cprintf("Alas, you cannot go that way."); }
else if(!strcmpi(arg,"west")||!strcmpi(arg,"w")) {if(room[cur_room].west) {cur_room=room[cur_room].west;look_room(cur_room);} else cprintf("Alas, you cannot go that way."); }
else if(!strcmpi(arg,"look")||!strcmpi(arg,"l")) {look_room(cur_room);}
else if(!strcmpi(arg,"score")||!strcmpi(arg,"sc")) {do_score();}
else if(!strcmpi(arg,"goto")) {int roomy;cprintf("Room:");cin>>roomy;cur_room=roomy;look_room(cur_room);}
else {textcolor(YELLOW);cprintf("Huh? \"%s\"\n\r",arg);}
strcpy(arg,"");
}

void do_score()
{
cprintf("Your players stats: %d/%d Hit Points, %d Experience.\n\r",ch.hit,ch.max_hit,ch.xp);
cprintf(" Prompt: %s"), ch.prompt;
}
//End of Troll_Attack.cpp programmed by Stephen Fluin