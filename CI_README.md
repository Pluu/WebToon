## CircleCI Local Build

1. Install Docker
2. Install CircleCI CLI
   - [Installing the CircleCI-Public CLI From Scratch on macOS and Linux Distros](https://circleci.com/docs/2.0/local-cli/#installing-the-circleci-public-cli-from-scratch-on-macos-and-linux-distros)

```
brew install circleci
```
```
curl https://raw.githubusercontent.com/CircleCI-Public/circleci-cli/master/install.sh \
	--fail --show-error | bash
```
3. Validate YAML
```
circleci config validate -c .circleci/config.yml
```
4. Running Build
```
circleci build .circleci/config.yml
```