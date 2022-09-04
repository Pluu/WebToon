## Gradle `dependencies` task 출력

단일 트리만 표시되도록 작업을 실행할 때 --configuration <이름>을 지정합니다.

```shell
./gradlew :app:dependencies --no-configuration-cache --configuration releaseRuntimeClasspath > new.txt
```

