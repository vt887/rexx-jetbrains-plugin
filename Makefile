.PHONY: help build clean test run runIde verify package check deps versions

GRADLEW := ./gradlew

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

build: ## Compile and build the plugin (without tests)
	$(GRADLEW) clean build -x test --no-daemon

clean: ## Remove all build artifacts
	$(GRADLEW) clean --no-daemon

test: ## Run all tests
	$(GRADLEW) test --no-daemon

run: ## Launch a sandboxed IDE with the plugin loaded
	$(GRADLEW) runIde

runIde: run ## Alias for 'run'

verify: ## Run plugin verifier against the current IDE
	$(GRADLEW) verifyPlugin --no-daemon

package: ## Build distributable plugin ZIP
	$(GRADLEW) buildPlugin --no-daemon

check: ## Run build + tests in one step
	$(GRADLEW) clean build test --no-daemon

deps: ## Print resolved dependency tree
	$(GRADLEW) dependencies --configuration runtimeClasspath --no-daemon

versions: ## Show versions of all relevant tools
	@echo "=== Java ===";        java -version 2>&1 || echo "not found"
	@echo "=== Kotlin ===";      kotlinc -version 2>&1 || echo "not found"
	@echo "=== Gradle ===";      $(GRADLEW) --version 2>&1 | grep -E "^(Gradle|Kotlin|Groovy|Ant|JVM|OS)" || echo "not found"
	@echo "=== Rexx ===";        rexx -v 2>&1 || regina --version 2>&1 || echo "not found"
	@echo "=== Git ===";         git --version 2>&1 || echo "not found"
