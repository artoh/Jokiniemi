# Suorituskykyvertailun tilastointi
# Tällä skriptillä voi tehdä suorittaa suorituskykyvertailun lukuisia kertoja
# ja saada tulosten jakauman (halutulla tarkkuudella ilmaistuna)
#
# esim
# python3 tilasto.py 1000 0 java -jar target/Jokiniemi-1.0-SNAPSHOT.jar test HA


import sys
import subprocess
import re

if len(sys.argv) < 3 :
	print("Käytä: tilasto [toistokerrat] [desimaalit] komento")
	quit()

d = {}

n = int(sys.argv[1])
desimaalit = int(sys.argv[2])

for i in range(0,n) :
	out = str(subprocess.check_output(sys.argv[3:]))	
	aikasearch = re.search("(\d+\\.\d+) s", out)
	
	if aikasearch:
		aikastr = aikasearch.group(1)
		aika = round(float(aikastr),desimaalit)
	
		if aika in d:
			d[aika] = d[aika] + 1
		else :
			d[aika] = 1
			
		print(".", end="")
		sys.stdout.flush()
	
print("\nTulokset")	
for k in sorted(d) :
	print("{}\t{}".format(k,d[k]))

