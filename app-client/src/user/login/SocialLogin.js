import React, { Component } from 'react';
import {GOOGLE_AUTH_URL} from "../../constants";
import googleLogo from '../../img/google-logo.png';

class SocialLogin extends Component {
    render() {
        return (
            <div className="social-login">
                <a className="btn btn-block social-btn google" href={GOOGLE_AUTH_URL}>
                    <img src={googleLogo} alt="Google" /> Log in with Google</a>
            </div>
        );
    }
}
export  default SocialLogin