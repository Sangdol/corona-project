.PHONY: ricotta

#
# local
#

ricotta:
	cd ricotta && lein run all local

ui:
	cd ricotta && lein run ui local

auto-ui:
	cd ricotta && lein auto run ui local

#
# prod
#

deploy: prod-init download-csv pull-ricotta run-ricotta generate-parmesan commit-push-parmesan clean-up
	echo "Deployed"

prod-init:
	mkdir -p data && mkdir -p ricotta/resources/web/dist

download-csv:
	wget "https://covid.ourworldindata.org/data/owid-covid-data.csv" -O "data/owid-covid-data.csv"
	echo "downloaded: data/owid-covid-data.csv"

pull-ricotta:
	git checkout master && git branch -D deploy
	git fetch origin deploy && \
		git checkout deploy && \
		echo "Git-pulled the deploy branch of ricotta"

# TODO log
run-ricotta:
	cd ricotta && lein run all prod

generate-parmesan:
	cp -r ricotta/resources/web/* ../parmesan
	echo "Parmesan generated"

commit-push-parmesan:
	cd ../parmesan && \
		git add . && \
		git commit -m "publish" && \
		git push && \
		echo "Parmesan pushed"

clean-up:
	# come back to master from deploy
	git checkout master
