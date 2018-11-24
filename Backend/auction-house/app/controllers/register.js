import Controller from '@ember/controller';

export default Controller.extend({

  nameErrorMessage: null,
  nameHasError: null,

  surnameErrorMessage: null,
  surnameHasError: null,

  emailErrorMessage: null,
  emailHasError: null,

  passwordErrorMessage: null,
  passwordHasError: null,

  actions: {
    createUser: function() {		
      let user = this.get('model.user');
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

          if(user.get('validations.attrs.name.messages') !== '' && user.get('validations.attrs.name.messages') !== null){
            this.set('nameHasError', true);
            this.set('nameErrorMessage', user.get('validations.attrs.name.messages'));
          }
          
          if(user.get('validations.attrs.surname.messages') !== '' && user.get('validations.attrs.surname.messages') !== null){
            this.set('surnameHasError', true);
            this.set('surnameErrorMessage', user.get('validations.attrs.surname.messages'));
          }
          
          if(user.get('validations.attrs.email.messages') !== '' && user.get('validations.attrs.email.messages') !== null){
            this.set('emailHasError', true);
            this.set('emailErrorMessage', user.get('validations.attrs.email.messages'));
          }

          if(user.get('validations.attrs.password.messages') !== '' && user.get('validations.attrs.password.messages') !== null){
            this.set('passwordHasError', true);
            this.set('passwordErrorMessage', user.get('validations.attrs.password.messages'));
          }

        }
      })
    },

    clearFields: function() {
      this.set('name', ''); 
      this.set('surname', '');
      this.set('email', '');
      this.set('password', '');
      this.set('nameHasError', null); 
      this.set('nameErrorMessage', null);
      this.set('surnameHasError', null); 
      this.set('surnameErrorMessage', null);
      this.set('emailHasError', null); 
      this.set('emailErrorMessage', null);
      this.set('passwordHasError', null); 
      this.set('passwordErrorMessage', null);
    }
  }
});
