import re

infile = open('myOut2.txt', 'r')
lines = infile.read().splitlines()

def split_by_sentence(line):
    wrong = ["mr.", "mrs.", "Aug.", "prof.", "vs.", "lt.", "jr.", "Esq.", "St.", "Sgt.", "II.", "III.", "I.", "Vol.", "Sr.", "Capt.", "Intl.", "Div.", "Jan.", "Feb."]
    r1 = re.findall(r"(\s(([a-zA-Z']{2,}?)|(w{3,}?))(\.|\?|\!)\s|\n)", line)
    for f in r1:
        found = f[0]
        isWrong = False
        for w in wrong:
            if w.lower() in found.lower():
                isWrong = True
                break
        if isWrong:
            continue
        line = re.sub(found, found + "\n", line)
    return line

def get_rid_of_spaces(line):
    return re.sub(r"\s\s",' ', line)

def get_rid_of_weird_start_chars(line):
    if not( bool(re.search(r"^[a-zA-Z']", line, re.IGNORECASE))):
        return line[1:]
    return line

def clean(line):
    old = "11r124124255151414122151251241"
    while old != line and line != "":
        old = line
        line = get_rid_of_spaces(line)
        line = get_rid_of_weird_start_chars(line)
    return line


i=0
ofile = open('myOut3.txt', 'w')
for line in lines:
    i+=1
    if i> 1000: break
    if len(line) < 3:
        continue
    #line = split_by_sentence(line)
    line = re.sub(r"[^A-Za-z'\s]", '', line)
    line = clean(line)
    ofile.write(line)
    ofile.write("\n")


