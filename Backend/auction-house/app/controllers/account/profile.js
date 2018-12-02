import Controller from '@ember/controller';

export default Controller.extend({ 
  selectedOption: null,

  nameHasError: null,
  nameErrorMessage: null,

  surnameHasError: null,
  surnameErrorMessage: null,

  dateOfBirthHasError: null,
  dateOfBirthErrorMessage: null,

  streetHasError: null,
  streetErrorMessage: null,

  cityHasError: null,
  cityErrorMessage: null,

  zipCodeHasError: null,
  zipCodeErrorMessage: null,

  stateHasError: null,
  stateErrorMessage: null,

  countryHasError: null,
  countryErrorMessage: null,


  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },

    updateUser: function() {
      let selectedOption = this.get('selectedOption');
      var _this = this;
      let user = this.get('model.user');
      let address = this.get('model.user.address');

      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('password', "Temporary123!@#");
      user.set('gender', this.get('selectedOption'));

      var bd = new Date(this.get('dateOfBirth'));
      bd.setMinutes(bd.getMinutes() - bd.getTimezoneOffset());
      user.set('dateOfBirth', bd);

      user.set('phoneNumber', this.get('phoneNumber'));

      address.set('street', this.get('street'));
      address.set('city', this.get('city'));
      address.set('zipCode', this.get('zipCode'));
      address.set('state', this.get('state'));
      address.set('country', this.get('country'));

      user.validate().then(({ validations }) => {
        if(validations.get('isValid')) {

          user.save().then(function(){
            _this.get('flashMessages').success('Updated Profile!');
          }).catch(function(){
            _this.get('flashMessages').errror('Oops! An unexpected error occoured.');
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
          
          if(user.get('validations.attrs.dateOfBirth.messages') !== '' && user.get('validations.attrs.dateOfBirth.messages') !== null){
            this.set('dateOfBirthHasError', true);
            this.set('dateOfBirthErrorMessage', user.get('validations.attrs.dateOfBirth.messages'));
          }            

          if(address.get('validations.attrs.street.messages') !== '' && address.get('validations.attrs.street.messages') !== null){
            this.set('streetHasError', true);
            this.set('streetErrorMessage', address.get('validations.attrs.street.messages'));
          }
    
          if(address.get('validations.attrs.city.messages') !== '' && address.get('validations.attrs.city.messages') !== null){
            this.set('cityHasError', true);
            this.set('cityErrorMessage', address.get('validations.attrs.city.messages'));
          }
          
          if(address.get('validations.attrs.zipCode.messages') !== '' && address.get('validations.attrs.zipCode.messages') !== null){
            this.set('zipCodeHasError', true);
            this.set('zipCodeErrorMessage', address.get('validations.attrs.dateOfBirth.messages'));
          }   

          if(address.get('validations.attrs.state.messages') !== '' && address.get('validations.attrs.state.messages') !== null){
            this.set('stateHasError', true);
            this.set('stateErrorMessage', address.get('validations.attrs.state.messages'));
          }
          
          if(address.get('validations.attrs.country.messages') !== '' && address.get('validations.attrs.country.messages') !== null){
            this.set('countryHasError', true);
            this.set('countryErrorMessage', address.get('validations.attrs.country.messages'));
          } 
        }
      });
    }
  }
});
