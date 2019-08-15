import glob
import os
import sys

PATH = sys.argv[1]
print PATH
FILES = glob.glob(PATH + '/*.java')

def label(f):
    label = raw_input()
    #json = f.replace('.java', '.json')
    if label is 't':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'static')
        #os.system('cp ' + json + ' ' + PATH + 'secure')
    elif label is 'l':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'length')
        #os.system('cp ' + json + ' ' + PATH + 'insecure')
    elif label is 'i':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'iter')
    elif label is 'c':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'cipher')
    elif label is 'r':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'random')
    elif label is 's':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'secure')
    elif label is 'o':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'other')
    elif label is 'n':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'skipped')
    elif label is 'q':
        print('--> Next samples')
        return True
    else:
        print('WARNING: malformed label input: ' + label)
        return False

    return False

def main():
    for i, f in enumerate(FILES):
        static = glob.glob(PATH + 'static/*.java')
        length = glob.glob(PATH + 'length/*.java')
        iteration = glob.glob(PATH + 'iter/*.java')
        cipher = glob.glob(PATH + 'cipher/*.java')
        random = glob.glob(PATH + 'random/*.java')
        secure = glob.glob(PATH + 'secure/*.java')
        other = glob.glob(PATH + 'other/*.java')
        skipped = glob.glob(PATH + 'skipped/*.java')

        print('-----------------------------------------------------------')

        f_name = os.path.basename(f)

        if any(f_name in os.path.basename(x) for x in static):
            print('WARNING: ' + f + ' already labeled as static')
            continue
        elif any(f_name in os.path.basename(x) for x in length):
            print('WARNING: ' + f + ' already labeled as length')
            continue
        elif any(f_name in os.path.basename(x) for x in iteration):
            print('WARNING: ' + f + ' already labeled as iter')
            continue
        elif any(f_name in os.path.basename(x) for x in cipher):
            print('WARNING: ' + f + ' already labeled as cipher')
            continue
        elif any(f_name in os.path.basename(x) for x in random):
            print('WARNING: ' + f + ' already labeled as random')
            continue
        elif any(f_name in os.path.basename(x) for x in secure):
            print('WARNING: ' + f + ' already labeled as secure')
            continue
        elif any(f_name in os.path.basename(x) for x in other):
            print('WARNING: ' + f + ' already labeled as other')
            continue
        elif any(f_name in os.path.basename(x) for x in skipped):
            print('WARNING: ' + f + ' skipped')
            continue

        print('Labeled ' + str(i) + ' from ' + str(len(FILES)) + ' samples, static/length/iter/cipher/random/secure/other/skipped(' + str(len(static)) + '/'  + str(len(length)) + '/' + str(len(iteration)) + '/'+ str(len(cipher)) + '/' + str(len(random)) + '/' + str(len(secure)) + '/' + str(len(other)) + '/' + str(len(skipped)) + ')')

        os.system('open ' + f)
        while(not label(f)):
            print('Wating for label input: static:t, length:l, iter:i, cipher:c, random:r, secure:s, other:o')

if __name__ == "__main__":
    main()
