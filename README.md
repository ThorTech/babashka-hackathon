# ThorTech Babashka Hackathon

## QuickStart

1. Install Babashka

   ```sh
   bash < <(curl -s https://raw.githubusercontent.com/babashka/babashka/master/install)
   ```

1. Ensure the main file is executable

   ```sh
   chmod +x src/thortech/main.clj
   ```

1. Execute the main file

   ```
   bb -m thortech.main
   ```

## Project Structure

```
├── LICENSE
├── README.md
├── deps.edn
├── src
│   └── thortech (namespace)
│       └── main.clj
└── test
    └── thortech (namespace)
        └── test_main.clj
```

## Resources

### Setup

- [Intellij Setup with Cursive](https://cursive-ide.com/userguide/babashka.html)
- [Visual Studio Code Calva Setup](https://calva.io/babashka/)

### Develop

- [Babashka Pods](https://github.com/babashka/pod-registry)
- [Babashka SQL Pods](https://github.com/babashka/babashka-sql-pods)

### Learn

- [Babashka Docs](https://github.com/babashka/babashka)
- [Babaska Examples](https://github.com/babashka/babashka/blob/master/examples/README.md)
- [Example Project Structure](https://cljdoc.org/d/borkdude/babashka/0.2.6/doc/readme)

# Team B

```clojure
(require '[com.grzm.awyeah.client.api :as aws])
(def lambda (aws/client {:api :lambda}))
(aws/invoke lambda {:op :Invoke
                    :request
                    {:FunctionName "arn:aws:lambda:us-east-1:138226511037:function:ConfigurationKeyRequests"}})
```

```shell
AWS_PROFILE=eas_dev AWS_DEFAULT_REGION=us-east-1 bb -m thortech.config -e 20221108 -t LegacyConfig
```