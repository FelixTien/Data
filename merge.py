def MergeSort(List, l, r):
    if l == r:
        return [List[l]]
    else:
        mid = l + (r - l)//2
        return Merge(MergeSort(List, l, mid), MergeSort(List, mid+1, r))

def Merge(AList,BList):
    temp = list()
    for i in range(len(AList)+len(BList)):
        if len(AList) == 0 or len(BList) == 0:
            temp.extend(AList)
            temp.extend(BList)
            break
        else:
            if AList[0] < BList[0]:
                temp.append(AList.pop(0))
            else:
                temp.append(BList.pop(0))
    return temp

A = [10, 2, 3, 6, 4, 9]
print(MergeSort(A,0,len(A)-1))