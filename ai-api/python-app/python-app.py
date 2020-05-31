import sys

i=1
while True:
    s = sys.stdin.readline().strip()
    sys.stdout.write(s.upper() + 'python OP' + str(i) + '\n')
    sys.stdout.flush()
    i+=1