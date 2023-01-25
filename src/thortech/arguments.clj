#!/usr/bin/env bb

(require '[clojure.tools.cli :refer [parse-opts]])

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main
  []
  (let [args (parse-opts *command-line-args* cli-options)]
    (cond (empty? (:errors args))
          (print (str "Using Port " (get-in args [:options :port]))))
          (print (:errors args))))

(-main)
