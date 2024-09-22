## Spring Shell

https://spring.pleiades.io/spring-shell/reference/

に沿ってSpring Shellの動作を確認


## 環境 
### Java 17
### Spring boot 3.3.4
### Spring Shell 3.3.2

## 実行

実行jarの作成
```
gradlew bootJar
```

```
java -jar shell-0.0.1-SNAPSHOT.jar
```

起動後にshell>が表示され、作成したコマンドを入力して動作を確認

## 動作にあたって

Intelljのターミナルでは動作しないので、Jarを作成して動作確認

また、
Windowsのターミナル（cmd）では動作しないので、jline:jline-terminal-jnaの依存関係を追加する（version 3.26.3はSpring Shell 3.3.2で使用しているjlineのversion）
   ```
   implementation 'org.jline:jline-terminal-jna:3.26.3'
   ```

上記は、spring shellのgithubで以下のissueに記載されている

https://github.com/spring-projects/spring-shell/issues/714

