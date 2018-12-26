import Controller from '@ember/controller';

export default Controller.extend({

  nameErrorMessage: null,
  nameHasError: null,

  surnameErrorMessage: null,
  surnameHasError: null,

  emailErrorMessage: null,
  emailHasError: null,

  emailConfirmationErrorMessage: null,
  emailConfirmationHasError: null,

  passwordErrorMessage: null,
  passwordHasError: null,

  passwordConfirmationErrorMessage: null,
  passwordConfirmationHasError: null,
  
  emailExistsErrorMessage: 'The input email is in usage',
  emailExistsHasError: null,


  actions: {
    createUser: function() {		
      let user = this.get('model.user');
      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('email', this.get('email'));
      user.set('password', this.get('password'));
      user.set('emailConfirmation', this.get('emailConfirmation'));
      user.set('passwordConfirmation', this.get('passwordConfirmation'));

      var _this = this;
      user.validate().then(({ validations }) =>{
        if(validations.get('isValid')){
          user.save().then(function() {
            _this.transitionToRoute('register-success')
          }).catch(function() {
            _this.set('emailExistsHasError', true);
            _this.set('nameHasError', null); 
            _this.set('nameErrorMessage', null);

            _this.set('surnameHasError', null); 
            _this.set('surnameErrorMessage', null);

            _this.set('emailHasError', null); 
            _this.set('emailErrorMessage', null);

            _this.set('passwordHasError', null); 
            _this.set('passwordErrorMessage', null);
          });
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

          if(user.get('validations.attrs.emailConfirmation.messages') !== '' && user.get('validations.attrs.emailConfirmation.messages') !== null){
            this.set('emailConfirmationHasError', true);
            this.set('emailConfirmationErrorMessage', user.get('validations.attrs.emailConfirmation.messages'));
          }

          if(user.get('validations.attrs.passwordConfirmation.messages') !== '' && 
            user.get('validations.attrs.passwordConfirmation.messages') !== null){
            this.set('passwordConfirmationHasError', true);
            this.set('passwordConfirmationErrorMessage', user.get('validations.attrs.passwordConfirmation.messages'));
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

      this.set('emailConfirmationHasError', null); 
      this.set('emailConfirmationErrorMessage', null);

      this.set('passwordConfirmationHasError', null); 
      this.set('passwordConfirmationErrorMessage', null);

      this.set('emailExistsHasError', null);
    }
  }
});
