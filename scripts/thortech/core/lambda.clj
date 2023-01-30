(ns thortech.core.lambda
  (:require [com.grzm.awyeah.client.api :as aws]
            [cheshire.core :as json]
            [clojure.java.io :as io]))

(def lambda-client
  (aws/client {:api :lambda}))

(defn- decode-json
  [buffered-input]
  (with-open [rdr (io/reader buffered-input)]
    (as-> (reduce conj [] (line-seq rdr)) v
          (first v)
          (json/decode v true))))

(defn invoke-lambda
  ([function-name request] (invoke-lambda function-name request json/encode decode-json))
  ([function-name request encoder] (invoke-lambda function-name request encoder decode-json))
  ([function-name request encoder decoder]
   (let [encoded (encoder request)
         result (aws/invoke lambda-client {:op      :Invoke
                                           :request {:FunctionName function-name
                                                     :Payload      encoded}})
         payload (:Payload result)]
     (decoder payload))))


(defn fire-and-forget
  ([function-name request] (invoke-lambda function-name request json/encode decode-json))
  ([function-name request encoder] (invoke-lambda function-name request encoder decode-json))
  ([function-name request encoder decoder]
   (let [encoded (encoder request)
         result (aws/invoke lambda-client {:op      :Invoke
                                           :request {:FunctionName function-name
                                                     :Payload      encoded}})])))