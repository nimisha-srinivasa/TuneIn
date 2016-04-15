hello.init({
    google: '36542558745-1l96ap825s047nutvan7j1jb6l29eiva.apps.googleusercontent.com'
}, 
{
    redirect_uri: 'index.html',
    scope: 'email'
},
{
	oauth_proxy: 'https://auth-server.herokuapp.com/proxy'
});

hello.on('auth.login', function(auth) {

    // Call user information, for the given network
    hello(auth.network).api('/me').then(function(r) {
        //do something
        user=r;
        window.location = "index.html";
    });
});

function login(){
	hello('google').login();    
}