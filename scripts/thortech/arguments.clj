(ns thortech.arguments)

(require '[clojure.tools.cli :refer [parse-opts]]
         '[babashka.cli :as cli])

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [parsed (parse-opts args cli-options)]
    (cond (empty? (:errors parsed))
          (print (str "Using Port " (get-in parsed [:options :port]))))
    (print (:errors parsed))))
