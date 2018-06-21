(ns aoc2016-clj.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (slurp (io/resource "day8.txt")))
(def instructions (str/split input #"\n"))
(defn make-screen [a b]
  (repeat a (repeat b 0)))

(map (partial re-seq #"\d+") instructions)
