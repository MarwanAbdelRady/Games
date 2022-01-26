import pygame
import random
import os
pygame.init()

WIDTH = 900
HEIGHT = 500
WIN = pygame.display.set_mode((WIDTH,HEIGHT))
BG = pygame.transform.scale(pygame.image.load(os.path.join('Images', 'background.jpg')), (WIDTH, HEIGHT))

pygame.display.set_caption("Fast Typer")
GAME_ICON = pygame.image.load('Images/keyboard.png')
pygame.display.set_icon(GAME_ICON)

FONT = pygame.font.SysFont("comicsans", 50)
END_FONT = pygame.font.SysFont('comicsans', 90)
WHITE = (255,255,255)
BLUE = (0,0,200)

START_BTN = pygame.Rect(375, 260, 150, 40)
START = 'Start'

RESET_BTN = pygame.Rect(700, 420, 150, 40)
RESET = 'Reset'

INPUT_BOX = pygame.Rect(350, 200, 140, 40)
COLOR_ACTIVE = pygame.Color('lightskyblue3')
COLOR_PASSIVE = pygame.Color('gray15')
COLOR_BOX = COLOR_PASSIVE

ACTIVE = False

WORD_LABEL = ''

INPUT_TEXT = ''

WATCH = 90
DT = pygame.time.get_ticks()

CURRENT_LEVEL = 0
CURRENT_SCORE = 0
COUNT = 0

level1 = []; level2 = []; level3 = []; level4 = []; level5 = []; level6 = []; level7 = []; level8 = []
leveltemp = []

f = open('Words/level1.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level1.append(element.strip())
f = open('Words/level2.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level2.append(element.strip())
f = open('Words/level3.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level3.append(element.strip())
f = open('Words/level4.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level4.append(element.strip())
f = open('Words/level5.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level5.append(element.strip())
f = open('Words/level6.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level6.append(element.strip())
f = open('Words/level7.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level7.append(element.strip())
f = open('Words/level8.txt')
leveltemp = f.readlines()
for element in leveltemp:
    level8.append(element.strip())

allLevels = [level1,level2,level3,level4,level5,level6,level7,level8]

def randomWord():
    input = allLevels[CURRENT_LEVEL]
    r = random.randint(0,len(input)-1)
    word = input[r]
    del input[r]
    return word

def level_counter():
    global COUNT, CURRENT_LEVEL
    COUNT += 1
    if COUNT == 5:
        CURRENT_LEVEL += 1
        COUNT = 0
    if CURRENT_LEVEL > 7:
        CURRENT_LEVEL -= 1

def final_score():
    box = pygame.Rect(100, 100, 700, 300)
    pygame.draw.rect(WIN, ((0,41,41)), box)
    msg = END_FONT.render('Game Over!',1, WHITE)
    WIN.blit(msg,(450 - msg.get_width()/2, 150))
    fs = END_FONT.render('Your Final Score is ' + str(CURRENT_SCORE), 1, WHITE)
    WIN.blit(fs,(450 - fs.get_width()/2, 250))
    pygame.display.update()

# def reset_game():
#     final_score()
#     pygame.time.delay(5000)
#     CURRENT_SCORE = 0
#     CURRENT_LEVEL = 0
#     COUNT = 0
#     WATCH = 90
#     ACTIVE = False
#     WORD_LABEL = randomWord()

def draw():
    WIN.blit(BG,(0,0))
    label = FONT.render(WORD_LABEL, 1, WHITE)
    WIN.blit(label,(450 - label.get_width()/2,150))
    timer = FONT.render('Time: ' + str(WATCH), 1, WHITE)
    WIN.blit(timer, (20,20))
    score = FONT.render('Score: ' + str(CURRENT_SCORE), 1, WHITE)
    WIN.blit(score, (880 - score.get_width(), 20))

    if ACTIVE:
        COLOR_BOX = COLOR_ACTIVE
    else:
        COLOR_BOX = COLOR_PASSIVE

    pygame.draw.rect(WIN,COLOR_BOX,INPUT_BOX)
    user_text = FONT.render(INPUT_TEXT,1,WHITE)
    WIN.blit(user_text,(INPUT_BOX.x + 5, INPUT_BOX.y + 5))
    INPUT_BOX.w = max(200,user_text.get_width() + 10)
    pygame.draw.rect(WIN,COLOR_BOX,START_BTN)
    btn_text = FONT.render(START,1,WHITE)
    WIN.blit(btn_text,(START_BTN.x + 35,START_BTN.y + 5))
    pygame.draw.rect(WIN, COLOR_BOX, RESET_BTN)
    reset_txt = FONT.render(RESET, 1, WHITE)
    WIN.blit(reset_txt,(RESET_BTN.x + 30,RESET_BTN.y + 5))
    pygame.display.update()

# Game loop
run = True
clock = pygame.time.Clock()
WORD_LABEL = randomWord()
while run:
    clock.tick(60)
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False
        
        if event.type == pygame.MOUSEBUTTONDOWN:
            if START_BTN.collidepoint(event.pos):
                ACTIVE = True
            else:
                ACTIVE = False
            if RESET_BTN.collidepoint(event.pos):
                final_score()
                pygame.time.delay(5000)
                CURRENT_SCORE = 0
                CURRENT_LEVEL = 0
                COUNT = 0
                WATCH = 90
                ACTIVE = False
                WORD_LABEL = randomWord()
        
        if event.type == pygame.KEYDOWN:
            if ACTIVE == True:
                if event.key == pygame.K_BACKSPACE:
                    INPUT_TEXT = INPUT_TEXT[:-1]
                else:
                    INPUT_TEXT += event.unicode
                if INPUT_TEXT == WORD_LABEL:
                    CURRENT_SCORE += 1
                    level_counter()
                    WORD_LABEL = randomWord()
                    INPUT_TEXT = ''
    if WATCH > 0 and ACTIVE:
        count_timer = pygame.time.get_ticks()
        if count_timer - DT > 1000:
            WATCH -= 1
            DT = count_timer
    elif WATCH == 0:
        final_score()
        pygame.time.delay(5000)
        CURRENT_SCORE = 0
        CURRENT_LEVEL = 0
        COUNT = 0
        WATCH = 90
        ACTIVE = False
        WORD_LABEL = randomWord()
    draw()