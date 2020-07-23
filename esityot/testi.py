# Tällä analysoidaan pelilautaa ja selvitetään jakauma sille,
# kuinka monella vuorolla päästään ruudusta toiseen.

import array
import re

a=array.array('i')
jakauma=array.array('i')

for i in range(0,200) :
    for k in range(0,200):
        a.append(9999)

for i in range(0,12):
    jakauma.append(0)


file = open("../src/main/resources/lauta.txt", mode='r')
for i in range(0,5):
    file.readline()
rivi = file.readline()
while rivi:
    nrot=re.split("\D+",rivi)
    mista=int(nrot[0])
    for n in range(1,len(nrot)):
        try:
            minne=int(nrot[n])
            a[mista*200+minne]=1
            a[minne*200+mista]=1
        except:
            pass
    rivi=file.readline()

for k in range(1,200):
    for i in range(1,200):
        for j in range(1,200):
            if(a[i*200+k]+a[k*200+j] < a[i*200+j]):
                a[i*200+j] = a[i*200+k] + a[k*200+j]

max=0
for i in range(1,200):
    for j in range(1,200):
        jakauma[a[i*200+j]]+=1
        if a[i*200+j] > max:
            max = a[i*200+j]
        if a[i*200+j]>9:
            # Ruudut, joista maksumaalinen 10 ruudun etäisyys
            print("{} - {}".format(i,j))


print(jakauma)


