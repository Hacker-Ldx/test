package com.microsoft.kiota.http;

import javax.annotation.Nonnull;

import com.microsoft.kiota.RequestOption;

/** Holds the tracing, metrics and logging configuration for the request adapter */
public class ObservabilityOptions implements RequestOption {
	private boolean includeEUIIAttributes;
	
	/**
	 * Gets whether to include attributes which could contains EUII information like URLs
	 */
	public boolean getIncludeEUIIAttributes() {
		return includeEUIIAttributes;
	}

	/**
	 * Sets whether to include attributes which could contains EUII information like URLs
	 * @param includeEUIIAttributes whether to include EUII attributes
	 */
	public void setIncludeEUIIAttributes(boolean includeEUIIAttributes) {
		this.includeEUIIAttributes = includeEUIIAttributes;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	@Nonnull
    public <T extends RequestOption> Class<T> getType() {
		return (Class<T>) ObservabilityOptions.class;
	}
	/** Gets the instrumentation name to use for tracing */
	@Nonnull
	public String GetTracerInstrumentationName() {
		return "com.microsoft.kiota:microsoft-kiota-http-okHttp";
	}
}
