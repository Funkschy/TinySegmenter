;; Clojure port of the genius TinySegmenter (http://chasen.org/~taku/software/TinySegmenter/)
;; by Taku Kudo <taku@chasen.org>.
;; This version is based on the python version of tinysegmenter:
;; https://github.com/SamuraiT/tinysegmenter/blob/master/tinysegmenter/tinysegmenter.py
;;
;; All credit goes to the original authors
(ns tinysegmenter.core
  (:require [tinysegmenter.scoremap :as s]))

(def ^:private char-patterns
  [[[\一 \龠] "H"]
   ["一二三四五六七八九十百千万億兆" "M"]
   ["々〆ヵヶ" "H"]
   [[\ぁ \ん] "I"]
   [[\ァ \ヴ] "K"]
   ["ーｰ\uff9e" "K"]
   [[\ｱ \ﾝ] "K"]
   [[\a \z] "A"]
   [[\A \Z] "A"]
   [[\ａ \ｚ] "A"]
   [[\Ａ \Ｚ] "A"]
   [[\0 \9] "N"]
   [[\０ \９] "N"]])

(def ^:private char-map
  (->> char-patterns
       (reduce (fn [m [pat c]]
                 (if (string? pat)
                   (reduce #(assoc! %1 %2 c) m pat)
                   (reduce #(assoc! %1 (char %2) c) m (range (int (pat 0)) (inc (int (pat 1)))))))
               (transient {}))
       (persistent!)))

(def ^:private bias -332)

(defn- segment-text [text]
  (let [seg    (conj (into ["B3" "B2" "B1"] text) "E1" "E2" "E3")
        ctype  (conj (into ["O" "O" "O"] (map #(get char-map % "O") text)) "O" "O" "O")
        end    (- (count seg) 3)
        word   (StringBuilder. 200)]
    (.append word (get seg 3))
    (loop [i 4, result [], p1 "U", p2 "U", p3 "U"]
      (if (< i end)
        (let [w1 (str (seg (- i 3)))
              w2 (str (seg (- i 2)))
              w3 (str (seg (- i 1)))
              w4 (str (seg i))
              w5 (str (seg (+ i 1)))
              w6 (str (seg (+ i 2)))
              c1 (ctype (- i 3))
              c2 (ctype (- i 2))
              c3 (ctype (- i 1))
              c4 (ctype i)
              c5 (ctype (+ i 1))
              c6 (ctype (+ i 2))
              score (+ bias
                       (get s/up1 p1 0)
                       (get s/up2 p2 0)
                       (get s/up3 p3 0)

                       (get s/bp1 (str p1 p2) 0)
                       (get s/bp2 (str p2 p3) 0)

                       (get s/uw1 w1 0)
                       (get s/uw2 w2 0)
                       (get s/uw3 w3 0)
                       (get s/uw4 w4 0)
                       (get s/uw5 w5 0)
                       (get s/uw6 w6 0)

                       (get s/bw1 (str w2 w3) 0)
                       (get s/bw2 (str w3 w4) 0)
                       (get s/bw3 (str w4 w5) 0)

                       (get s/tw1 (str w1 w2 w3) 0)
                       (get s/tw2 (str w2 w3 w4) 0)
                       (get s/tw3 (str w3 w4 w5) 0)
                       (get s/tw4 (str w4 w5 w6) 0)

                       (get s/uc1 c1 0)
                       (get s/uc2 c2 0)
                       (get s/uc3 c3 0)
                       (get s/uc4 c4 0)
                       (get s/uc5 c5 0)
                       (get s/uc6 c6 0)

                       (get s/bc1 (str c2 c3) 0)
                       (get s/bc2 (str c3 c4) 0)
                       (get s/bc3 (str c4 c5) 0)

                       (get s/tc1 (str c1 c2 c3) 0)
                       (get s/tc2 (str c2 c3 c4) 0)
                       (get s/tc3 (str c3 c4 c5) 0)
                       (get s/tc4 (str c4 c5 c6) 0)

                       (get s/uq1 (str p1 c1) 0)
                       (get s/uq2 (str p2 c2) 0)
                       (get s/uq3 (str p3 c3) 0)

                       (get s/bq1 (str p2 c2 c3) 0)
                       (get s/bq2 (str p2 c3 c4) 0)
                       (get s/bq3 (str p3 c2 c3) 0)
                       (get s/bq4 (str p3 c3 c4) 0)

                       (get s/tq1 (str p2 c1 c2 c3) 0)
                       (get s/tq2 (str p2 c2 c3 c4) 0)
                       (get s/tq3 (str p3 c1 c2 c3) 0)
                       (get s/tq4 (str p3 c2 c3 c4) 0))
              next-i (long (inc i))
              w      (str word)]
          (if (> score 0)
            (do
              (.setLength word 0)
              (.append word (seg i))
              (recur next-i (conj result w) p2 p3 "B"))
            (do (.append word (seg i))
                (recur next-i result p2 p3 "O"))))
        (conj result (str word))))))

(defn segment [text]
  (if (empty? text)
    nil
    (segment-text text)))
