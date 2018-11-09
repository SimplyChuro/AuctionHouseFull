import Controller from '@ember/controller';

export default Controller.extend({
actions: {
	  createUser: function() {
	    this.store.createRecord('user', {
	    	name: this.get('name'),
	      	surname: this.get('surname'),
	      	email: this.get('emailAddress'),
	      	password: this.get('password')
		}).save();
		this.transitionToRoute('register-success');
	  }
	}
});
