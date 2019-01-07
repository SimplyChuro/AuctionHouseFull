import ENV from 'auction-house/config/environment';
import FileField from 'ember-uploader/components/file-field';
import S3Uploader from 'ember-uploader/uploaders/s3';
import { inject as service } from '@ember/service';

export default FileField.extend({
  signingUrl: ENV.HOST_URL+'/api/v1/validate',
  session: service(),

  multiple: true,

  filesDidChange(files) {
    const uploader = S3Uploader.create({
      signingUrl: this.get('signingUrl'),
      signingAjaxSettings: {
        headers: {
          'X-AUTH-TOKEN': this.get('session').authToken
        }
      }
    });

    uploader.on('progress', e => {
      var _this = this;
      _this.sendAction('onProgress', e);
    });

    uploader.on('didUpload', response => {
      let uploadedUrl = $(response).find('Location')[0].textContent;
      uploadedUrl = decodeURIComponent(uploadedUrl);

      var _this = this;
      _this.sendAction('onComplete', {image: uploadedUrl});

    });

    if (!Ember.isEmpty(files)) {
      uploader.upload(files);
    }
  }
});