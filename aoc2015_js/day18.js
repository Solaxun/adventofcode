const _  = require('lodash')
const fs = require('fs')

var input     = fs.readFileSync('day14.txt').toString()
var SECONDS   = 2503
var deer      = _.chunk(input.match(/\d+/g).map(x => parseInt(x)),3)
var traveled  = seconds => ([d,s,r]) => (Math.floor(seconds / (s + r))  * d * s +
                                         Math.min(s,seconds % (s + r)) * d)
// part 1
var winner   = Math.max(..._.map(deer,traveled(SECONDS)))
console.log(winner)

// part 2
var points = Array(9).fill(0)
var maxDist
for (var i = 1; i < SECONDS + 1; i++){
    let traveledSeconcs = traveled(i)
    maxDist = deer.reduce((prev,cur) => traveledSeconcs(cur) > traveledSeconcs(prev) ? cur : prev)
    winningIndex = deer.indexOf(maxDist)
    points[winningIndex] += 1 
}
console.log(Math.max(...points))
