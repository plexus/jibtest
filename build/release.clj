(ns release
  (:require [clojure.tools.build.api :as b]
            [juxt.pack.jib :as jib]))


(def lib 'my/lib1)
(def version (format "1.2.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"
                            :aliases [:main]}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn prep [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  #_(b/copy-dir {:src-dirs ["src" "resources"]
                 :target-dir class-dir}))

(defn jar [opts]
  (prep opts)
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn container [opts]
  (prep opts)
  (jib/jib {:basis basis
            :base-image "gcr.io/distroless/java17-debian11" #_"azul/zulu-openjdk-alpine:17.0.2-jre" #_"eclipse-temurin:18-alpine"
            :env {}
            :image-type :docker #_:tar #_:registry
            #_:tar-file #_"release.tar"
            :image-name "jibtest"
            :labels {}
            :tags []
            :user "root:root" ;;?
            #_#_            :layers [:libs
                                     ]
            })
  )
