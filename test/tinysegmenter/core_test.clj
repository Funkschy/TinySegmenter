(ns tinysegmenter.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [tinysegmenter.core :refer [segment]]))

;; most of the test cases are taken from https://github.com/SamuraiT/tinysegmenter/blob/master/tests/
(deftest test-segment
  (testing "single sentence"
    (is (nil? (segment "")))
    (is (= (segment "私の名前は中野です") ["私" "の" "名前" "は" "中野" "です"]))
    (is (= (segment "今日は良い天気ですね") ["今日" "は" "良い" "天気" "です" "ね"]))
    (is (= (segment "TinySegmenterは25kBで書かれています。") ["TinySegmenter" "は" "2" "5" "kB" "で" "書か" "れ" "て" "い" "ます" "。"]))
    (is (= (segment "私はclojure大好きprogrammerです") ["私" "は" "clojure" "大好き" "programmer" "です"]))))
