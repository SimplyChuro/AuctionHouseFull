import Controller from '@ember/controller';

export default Controller.extend({
	actions: {
	  login: function() {
		Ember.$.ajax({
            url: 'http://localhost:9000/api/v1/login',
            type: 'POST',
            data: JSON.stringify({
                email: this.get('emailAddress'),
                password: this.get('password')
            }),
            contentType: 'application/json;charset=utf-8',
            dataType: 'json'
        }).then(function(response) {
            Ember.run(function() {
                resolve({
                    token: response.authToken
                });
            });
        }, function(xhr, status, error) {
            var response = xhr.responseText;
            Ember.run(function() {
                reject(response);
            });
        });

	  }
	}
});
