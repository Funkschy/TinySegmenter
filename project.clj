(defproject tinysegmenter "0.1.0-SNAPSHOT"
  :description "A Clojure library to split Japanese into words"
  :license {:name "BSD 3-Clause 'New' or 'Revised' License"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :global-vars {*warn-on-reflection* true}
  :repl-options {:init-ns tinysegmenter.core})
