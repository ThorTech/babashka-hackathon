(ns thortech.config
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [com.grzm.awyeah.client.api :as aws]))

(def cli-options
  ;; An option with a required argument
  [["-e" "--env ENVIRONMENT" "Environment name, e.g. 20221108"
    ;; :default ""
    ;; :parse-fn #(Integer/parseInt %)
    ;; :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]
    ]
   ["-t" "--tokens TOKEN" "Token name, e.g. LegacyConfig"
    :multi true
    :default []
    :update-fn conj]
   ["-k" "--key KEY" "Key name, e.g. DatabaseName"]
   ["-h" "--help"]])

(defn get-token
  ([env tokens]
   (get-token (aws/client {:api :lambda}) env tokens))
  ([client env tokens]
   (let [params (json/encode {:requestType "Query"
                              :compressResults false
                              :environmentName env
                              :tokenNames (into [] tokens)})
         payload (io/input-stream (.getBytes params))
         out (aws/invoke client {:op :Invoke
                                 :request
                                 {:FunctionName "ConfigurationKeyRequests"
                                  :Payload payload}})]
     (if (:Payload out)
       (with-open [r (io/reader (:Payload out))] 
         (doall (-> r (json/parse-stream true) :Values)))
       (do 
         (println out)
         (throw (ex-info "No :Paylod found in output from lambda invoke." {:data out})))))))

(defn get-value
  ([env token key]
   (get-value (aws/client {:api :lambda}) env token key))
  ([client env token key]
   (let [token-data (get-token client env token)
         key (str/lower-case key)]
     (some #(when (= (str/lower-case (:KeyName %)) key)
              (:Value %)) token-data))))

(defn exit
  "Helper to wrap System/exit"
  [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond (:help options)
          (exit 0 summary)

          (empty? errors)
          (if (:key options)
            (get-value (:env options)
                       (:tokens options)
                       (:key options))
            (get-token (:env options)
                       (:tokens options))))))