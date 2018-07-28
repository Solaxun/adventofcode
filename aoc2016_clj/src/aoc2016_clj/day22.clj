(ns aoc-2016.aoc22
    (:require [clojure.string :as str]
              [clojure.math.combinatorics :as combs]
              [clojure.set :as set]
              [clojure.repl :refer [source]]
              [clojure.java.io :as io]))

(def data (-> "day22.txt" io/resource slurp))

(defn parse [data]
  (->> (re-seq #"\d+" data)
       (map read-string)
       (partition 6)))

(defn make-node [nums]
  (zipmap [:x :y :size :used :avail :used-pct] nums))

(defn make-grid [data]
  (->> data
       parse
       (map make-node)))

(def grid (make-grid data))

(def max-x (apply max-key :x grid))
(def max-y (apply max-key :y grid))
(def goal-node (first (filter (fn [{x :x y :y}]
                                (and (= x (:x max-x)) (zero? y))) grid)))
(def empty-node (first (filter #(zero? (:used %)) grid)))
goal-node
empty-node


(defn print-board [board]
  (doseq [ln board]
    (println ln)))

;; start from the empty node, neighbors are adjacents that can fit into it
(defn neighbors [{x :x y :y}]
  (for [mx [-1 0 +1] my [-1 0 +1]
        :when (not= (Math/abs mx) (Math/abs my))]
    [(+ x mx) (+ y my)]))

(def back-row  (str/split "RKBQKBKR" #""))
(def front-row (repeat 8 "P"))
(def board
  (->> (concat back-row front-row
               (repeat 32 nil)
               (map str/lower-case front-row) ; black is lower-case
               (map str/lower-case back-row))
       (partition 8)
       (mapv vec)))

(def game {:board board
           :white-captured nil
           :black-captured nil
           :player-turn "white"})

(defmulti move #((juxt :player-turn )))

(defn move [from to board]
  (let [attacker (get-in board from)
        defender (get-in board to)]
    (-> board
        (assoc-in to attacker)
        (assoc-in from nil))))

(defn pawn-move [[r c] color]
  (if (= color "white")
    (vector [(inc r) c] [(+ 2 r) c])
    (vector [(dec r) c] [(- 2 r) c])))

(defn in-bounds? [[x y]]
  (and (>= x 0) (>= y 0) (<= y 8) (<= x 8)))

(defn iterate-till-end [[x y] [dx dy]]
  (->> (iterate (fn [[x y]] [(+ x dx) (+ y dy)]) [x y])
       (take-while in-bounds?)))

(defn legal-moves [loc move-type]
  (let [diag-moves [[1 -1][1 1][-1 -1][-1 1]]
        uldr-moves [[1 0][-1 0][0 1][0 -1]]]
    (apply concat (for [m diag-moves]
                    (iterate-till-end loc m)))))

(legal-moves [3 4] "fo")
