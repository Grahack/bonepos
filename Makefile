dev:
	lein fig:build
prod:
	chromium --headless --dump-dom http://localhost:9500/ > index.html
	sed '12d' -i index.html  # remove line 12 three times
	sed '12d' -i index.html  # removes the JS code for Fighwheel
	sed '12d' -i index.html  # and some empty lines
	git add index.html
	git commit -m "Update prod"
	git push origin main
