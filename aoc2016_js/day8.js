const _ = require('lodash')
const fs = require('fs')
var Screen = makeScreen(50,6)
var input = fs.readFileSync('day8.txt').toString()

function makeScreen(a,b){
  let screen = []
  for (var i = 0; i < b; i++){
    row = Array(50).fill(0)
    screen.push(row)
  }
  return screen
}

function turnOn(screen,a,b){
  //turn everything on in top left that is 'A' wide and 'B' tall
  for (let i = 0; i < b; i++){
    for (let j = 0; j < a; j++){
      screen[i][j] = 1
    }
  }
}

function displayScreen(screen){
  screen.forEach(x => console.log(x.toString().replace(/0/g,'.')))
}

function rotateRow(screen,a,b){
  // try to move everything in row 'A' right 'B' times, wrapping to left
  let row = screen[a]
  let splitPoint =  row.length - b % row.length
  row = row.slice(splitPoint).concat(row.slice(0,splitPoint))
  screen[a] = row
}

function rotateColumn(screen,a,b){
  // try to move everything in col 'A' down 'B' times, wrapping to top
  let col = screen.map(row => row[a])
  let splitPoint =  col.length - b % col.length 
  col = col.slice(splitPoint).concat(col.slice(0,splitPoint))
  // get each a in row, and simultaneously change to nth col element
  for (let i = 0; i < screen.length; i++){
    screen[i][a] = col[i]
  }
}

function parseInstructions(instr){
  let rules = instr.split(' ')
  let [a,b] = _.map(instr.match(/\d+/g),n => parseInt(n))

  if (rules[0] == 'rotate'){
    itemToRotate = rules[1]
    if (itemToRotate == 'column'){
      rotateColumn(Screen,a,b)
    }
    else{
      rotateRow(Screen,a,b)
    }
  }
  else{
    turnOn(Screen,a,b)
  }
}

function countOn(Screen){
  var rowCounts = Screen.map(row => row.reduce((a,b) => b == 1 ? a + b : a))
  return rowCounts.reduce((a,b) => a + b)
}

instructions = input.split('\n')
instructions.forEach(parseInstructions)

console.log(countOn(Screen))
console.log()
displayScreen(Screen)