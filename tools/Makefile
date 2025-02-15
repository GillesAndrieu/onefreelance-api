SHELL := /bin/bash
THREADS ?= 50
RAMP_UP ?= 5

bootstrap: header clear-log install
	@echo "OneFreelance is ready to run."
	@echo "You can run it by either:"
	@echo "    - using 'make ide' and run the applications manually by launching API from within IntelliJ"
	@echo "    - using 'make all' and run the applications manually by launching API from within IntelliJ with all tools"

ide: clear-log
	docker-compose up postgres

all: clear-log
	docker-compose up postgres pgadmin

install:
	@printf "Installing application dependencies using Gradle..."
	@../gradlew clean build -p ../ >> ./.log/.makelog 2>&1 & ./.meta/spinner.sh
	@echo -e "\Application dependencies installed successfully."

clear-log:
	@mkdir -p ./.log 2>&1
	@printf '' > ./.log/.makelog 2>&1

header:
	@echo "   ____             ______             _                       "
	@echo "  / __ \           |  ____|           | |                      "
	@echo " | |  | |_ __   ___| |__ _ __ ___  ___| | __ _ _ __   ___ ___  "
	@echo " | |  | | |_ \ / _ \  __| |__/ _ \/ _ \ |/ _| | |_ \ / __/ _ \ "
	@echo " | |__| | | | |  __/ |  | | |  __/  __/ | (_| | | | | (_|  __/ "
	@echo "  \____/|_| |_|\___|_|  |_|  \___|\___|_|\__,_|_| |_|\___\___| "
