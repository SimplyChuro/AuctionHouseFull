import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    createUser: function() {		
      let user = this.get('model.user');
      this.set('user', user);
      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('email', this.get('emailAddress'));
      user.set('password', this.get('password'));
      user.validate().then(({ validations }) =>{
        if(validations.get('isValid')){
          user.save().then(
            this.transitionToRoute('register-success')
          ).catch();
        } else {

        }
      })
    },
    clearFields: function() {
      this.set('name', ''); 
      this.set('surname', '');
      this.set('email', '');
      this.set('password', '');
    }
  }
});
