.PHONY: all download-csv

#
# local
#

ricotta:
	cd ricotta && lein run local

#
# prod
#

deploy: pull-ricotta run-ricotta generate-parmesan commit-push-parmesan
	@echo "Deployed"

download-csv:
	@wget "https://covid.ourworldindata.org/data/owid-covid-data.csv" -O "data/owid-covid-data.csv"
	@echo "downloaded: data/owid-covid-data.csv"

pull-ricotta:
	@git pull
	@echo "Git-pulled ricotta"

# TODO log
run-ricotta:
	cd ricotta && lein run prod

generate-parmesan:
	@cp -r ricotta/resources/web/* ../parmesan
	@echo "Parmesan generated"

commit-push-parmesan:
	cd ../parmesan && \
		git add . && \
		git commit -m "publish" && \
		git push && \
		echo "Parmesan pushed"
