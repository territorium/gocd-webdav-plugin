# GoCD WebDAV Plugin

This GoCD WebDAV plugin supports uploading file to a WebDAV server.

## Requirements

* GoCD 18 (could work with earlier versions, as low as 14)

## Getting started

- define WEBDAV_URL as a variable, for the remote location
- define WEBDAV_USERNAME as a variable (optional)
- define WEBDAV_PASSWORD as a (secure) variable (optional)
- add a job/task and configure:
- the artifact you wish to upload

## Building the code base

To build the jar, run `./gradlew clean test assemble`

## License

```plain
Copyright 2018 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
