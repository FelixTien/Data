const month = 7 + 0;
const day = 23;



const due = 25;
const sid = month - 1
var id = 0;
if(day > due){
    id = month - 1
}else{
    id = month == 1 ? 11 : month - 2
}

console.log(id%3);