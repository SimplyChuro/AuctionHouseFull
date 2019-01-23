import Controller from '@ember/controller';

export default Controller.extend({

  currentCategory: null,
  categoryInputEnabled: false,
  subCategoryInputEnabled: false,

  categoryNameHasError: null,
  categoryNameErrorMessage: null,

  subCategoryNameHasError: null,
  subCategoryNameErrorMessage: null,

  actions: {
    toggleDetails: function(category) {
      var checker = this.get('currentCategory');

      if((checker === null) || (category.id !== checker.id)) {
        this.set('subCategoryInputEnabled', false);
        this.set('currentCategory', category);
      } else {
        this.set('currentCategory', null);
      }

    },

    toggleCategoryInput: function() {
      this.set('categoryInputEnabled', true);
    },

    toggleSubCategoryInput: function() {
      this.set('subCategoryInputEnabled', true);
    },

    async createParentCategory() {
      var _this = this;

      let category = this.store.createRecord('category');
      category.set('name', this.get('parentNameInput'));
      category.set('parent_id', null);
      await category.save().then(function(){
        _this.set('parentNameInput', '');
        _this.set('categoryInputEnabled', false);
      });
    },

    async createChildCategory() {
      var _this = this;

      let category = this.store.createRecord('category');
      category.set('name', this.get('childNameInput'));
      category.set('parent_id', this.get('currentCategory').id);
      await category.save().then(function(){
        _this.set('childNameInput', '');
        _this.set('subCategoryInputEnabled', false);
      });
    },

    async deleteParentCategory(category) {
      await category.destroyRecord();
    },

    async deleteChildCategory(category) {
      await category.destroyRecord();
    },

    clearFields: function() {
      this.set('currentCategory', null);
      this.set('categoryInputEnabled', false);
      this.set('subCategoryInputEnabled', false);
    }

  }

});
