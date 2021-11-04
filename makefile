.PHONY: ricotta

#
# local
#

test:
	cd ricotta && lein test-refresh

ricotta:
	cd ricotta && lein run all local

ui:
	cd ricotta && lein run ui local

auto-ui:
	cd ricotta && lein auto run ui local

ready:
	git push origin master:deploy

#
# prod
#

fe-deploy: pull-ricotta run-ricotta generate-parmesan commit-push-parmesan
	echo "Front-end deployed"

deploy: prod-init download-csv pull-ricotta run-ricotta generate-parmesan commit-push-parmesan
	echo "Deployed"

prod-init:
	mkdir -p data && mkdir -p ricotta/resources/web/dist

download-csv:
	wget "https://covid.ourworldindata.org/data/owid-covid-data.csv" -O "data/owid-covid-data.csv"
	echo "downloaded: data/owid-covid-data.csv"

pull-ricotta:
	git branch -D deploy &>/dev/null
	git checkout master && \
		git pull && \
		git fetch origin deploy && \
		git checkout deploy && \
		echo "Git-pulled the deploy branch of ricotta"

# TODO log
run-ricotta:
	cd ricotta && lein run all prod

generate-parmesan:
	cp -r ricotta/resources/web/* ../parmesan
	echo "Parmesan generated"

# TODO if there's a change in the origin this will fail.
#
# "git update-index --refresh" not to detect changes of the same file
# https://stackoverflow.com/questions/34807971/why-does-git-diff-index-head-result-change-for-touched-files-after-git-diff-or-g
commit-push-parmesan:
	cd ../parmesan && \
		git update-index --refresh && \
		git diff-index --quiet HEAD || \
			(git add . && \
			git commit -m "publish" && \
			git push && \
			echo "Parmesan pushed")

clean-up:
	# come back to master from deploy
	git checkout .
	git checkout master
