dev:
	lein fig:build
prod:
	chromium-browser --headless --dump-dom http://localhost:9500/ > index.html
	sed '1d'  -i index.html
	sed '14d' -i index.html
	git add index.html
	git commit -m "Update prod"
	git push origin main
