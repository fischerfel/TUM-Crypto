import glob
import os
import sys

PATH = sys.argv[1]
print PATH
FILES = glob.glob(PATH + '/*.java')

def label(f):
    label = raw_input()
    #json = f.replace('.java', '.json')
    if label is 'a':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'array')
        #os.system('cp ' + json + ' ' + PATH + 'secure')
    elif label is 'e':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'empty')
        #os.system('cp ' + json + ' ' + PATH + 'insecure')
    elif label is 'g':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'getbytes')
    elif label is 'o':
        print('--> Label ' + label)
        os.system('cp ' + f + ' ' + PATH + 'other')
    else:
        print('WARNING: malformed label input: ' + label)
        return False

    return True

def main():
    for i, f in enumerate(FILES):
        array = glob.glob(PATH + 'array/*.java')
        empty = glob.glob(PATH + 'empty/*.java')
        getbytes = glob.glob(PATH + 'getbytes/*.java')
        other = glob.glob(PATH + 'other/*.java')

        print('-----------------------------------------------------------')

        f_name = os.path.basename(f)

        if any(f_name in os.path.basename(x) for x in array):
            print('WARNING: ' + f + ' already labeled as array')
            continue
        elif any(f_name in os.path.basename(x) for x in empty):
            print('WARNING: ' + f + ' already labeled as empty')
            continue
        elif any(f_name in os.path.basename(x) for x in getbytes):
            print('WARNING: ' + f + ' already labeled as getbytes')
            continue
        elif any(f_name in os.path.basename(x) for x in other):
            print('WARNING: ' + f + ' already labeled as other')
            continue

        print('Labeled ' + str(i) + ' from ' + str(len(FILES)) + ' samples, array/empty/getbytes/other (' + str(len(array)) + '/' + str(len(empty)) + '/' + str(len(getbytes)) + '/' + str(len(other)) +')')

        os.system('open ' + f)
        while(not label(f)):
            print('Wating for label input')

if __name__ == "__main__":
    main()
