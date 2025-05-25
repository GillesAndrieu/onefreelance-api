<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Create credentials](#create-credentials)
- [Configuration branding](#configuration-branding)
- [Configuration javascript origins](#configuration-javascript-origins)

# Create credentials

Use google documentation for create new application and credentials for login :
- https://developers.google.com/workspace/guides/create-credentials?hl=en

# Configuration branding

To use the postman collection it's necessary to authorize the domains.
Go to `Branding` section and add this two domains : 
- `getpostman.com`
- `oauth.pstmn.io`

# Configuration javascript origins

Now, config the host authorized in you front. Add this uris in your Google Cloud application : 
- `http://localhost:5173` for locale launcher
- `http://localhost:5100` for docker launcher

# Configuration redirect uris

For the end, config the uris after login redirect in your Google Cloud application : 
- `https://www.getpostman.com/oauth2/callback` for postman
- `https://oauth.pstmn.io/v1/browser-callback` for postman
- `https://oauth.pstmn.io/v1/callback` for postman
- `http://localhost:8080/login/oauth2/code/google` for your back application
- `http://localhost:8080` for your back application
- `http://localhost:5173` for your front application run on locale
- `http://localhost:5100` for your front application run on docker