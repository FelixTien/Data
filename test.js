var month = 7 + 0;
var day = 23;
var due = 25;
var sid = month - 1;
var id = 0;
if (day > due) {
    id = month - 1;
}
else {
    id = month == 1 ? 11 : month - 2;
}
console.log(id % 3);
