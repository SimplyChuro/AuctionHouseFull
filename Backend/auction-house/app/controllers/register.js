import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    createUser: function() {		
      let record = this.store.createRecord('user');

      record.set('name', this.get('name'));
      record.set('surname', this.get('surname'));
      record.set('email', this.get('emailAddress'));
      record.set('password', this.get('password'));
      record.save().then(
        this.transitionToRoute('register-success')
        ).catch();
    }
  }
});
