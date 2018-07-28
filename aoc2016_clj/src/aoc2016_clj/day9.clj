(ns aoc-2016.aoc9
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combs]
            [clojure.set :as set]
            [clojure.repl :refer [source]]
            [clojure.java.io :as io]))

(def data (-> "day9.txt" io/resource slurp str/trim))

;; part 1
(defn expand-rule [txt i j]
  (map read-string (-> txt (subs (inc i) j) (str/split #"x"))))

(defn process [data]
  (loop [txt data
         i 0
         lp 0]
    (cond (>= i (count txt))
          txt
          (= (get txt i) \( )
          (recur txt (inc i) i)
          (= (get txt i) \) )
          (let [[nchars times] (expand-rule txt lp i)
                expanded-txt (apply str
                                    (repeat times (subs txt (inc i) (+ i 1 nchars))))
                still-needs-expand (subs txt (+ nchars (inc i)))
                new-txt (str (subs txt 0 lp) expanded-txt still-needs-expand)]
            (recur new-txt
                   (- (count new-txt) (count still-needs-expand))
                   0))
          :else
          (recur txt (inc i) lp))))

(count (process data))

;; part 2
(defn extract-rule [subv]
  (let [[n _ x] (partition-by #(= % \x) subv )]
    (map (comp read-string str/join) [n x])))

(defn decompress [text]
  (if-let [[x & xs] (seq text)]
    (if (= \( x)
      (let [rp (.indexOf xs \))
            xs (vec xs)
            [chars rpt] (extract-rule (subvec xs 0 rp))
            left (str/join (subvec xs (inc rp) (+ 1 rp chars)))
            right (str/join (subvec xs (+ 1 rp chars)))]
        (+ (* rpt (decompress left))
           (decompress right)))
      (+ 1 (decompress (subs text 1))))
    0))

(decompress data)
