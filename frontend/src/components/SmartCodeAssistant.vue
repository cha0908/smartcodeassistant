<template>
  <div class="smart-code-assistant">
    <!-- Fixed Header Section -->
    <div class="header-section">
      <h1>{{ title }}</h1>
      <div class="description">
        <h2>About Smart Code Assistant</h2>
        <p>
          Welcome to your Smart Code Assistant application! This tool helps
          developers write better code through AI-powered assistance using
          Ollama Qwen2.5 Coder LLM.
        </p>
      </div>
    </div>

    <div class="main-container">
      <!-- Scrollable Chat Section -->
      <div class="chat-container">
        <div class="chat-history" ref="chatHistory">
          <div
            v-for="(message, index) in chatHistory"
            :key="index"
            class="chat-message"
            :class="message.sender"
          >
            <div class="message-header">
              <strong>{{
                message.sender === "user" ? "You" : "Smart Code Assistant"
              }}</strong>
              <span class="message-time">{{
                formatTime(message.timestamp)
              }}</span>
            </div>
            <div class="message-content">
              <pre v-if="message.sender === 'assistant'">{{
                message.content
              }}</pre>
              <p v-else>{{ message.content }}</p>
            </div>
          </div>
        </div>

        <div class="loading-section" v-if="isLoading">
          <div class="loading-spinner"></div>
          <p>Please wait while Qwen2.5 Coder processes your request</p>
        </div>

        <div class="error-section" v-if="error">
          <h2>Error</h2>
          <div class="error-message">{{ error }}</div>
          <button @click="resetError" class="reset-button">Try Again</button>
        </div>
      </div>

      <!-- Fixed Input Section -->
      <div class="input-section">
        <textarea
          v-model="userPrompt"
          placeholder="Enter your code question or prompt here..."
          class="prompt-input"
          rows="3"
          :disabled="isLoading"
          @keydown.ctrl.enter="generateCode"
          @keydown.meta.enter="generateCode"
        ></textarea>
        <div class="action-buttons">
          <button
            @click="generateCode"
            class="action-button"
            :disabled="isLoading || !userPrompt.trim()"
          >
            {{ isLoading ? "Generating..." : "Send Message" }}
          </button>
          <button
            @click="clearChat"
            class="reset-button"
            :disabled="isLoading || chatHistory.length === 0"
          >
            Clear Chat
          </button>
        </div>
        <div class="input-help">
          <small>Press Ctrl+Enter (or Cmd+Enter on Mac) to send message</small>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// Define API constants - Using proxied API path
const API_BASE_URL = "/api";

export default {
  name: "SmartCodeAssistant",
  data() {
    return {
      title: "Smart Code Assistant",
      userPrompt: "",
      isLoading: false,
      error: null,
      chatHistory: [],
    };
  },
  methods: {
    async generateCode() {
      if (!this.userPrompt.trim() || this.isLoading) {
        return;
      }

      // Add user message to chat history
      this.addMessageToChat("user", this.userPrompt);

      // Store and clear the prompt
      const currentPrompt = this.userPrompt;
      this.userPrompt = "";

      this.isLoading = true;
      this.error = null;

      try {
        console.log(
          "Sending request to:",
          `${API_BASE_URL}/assistant/generate`
        );

        // Direct API call to proxied endpoint
        const response = await fetch(`${API_BASE_URL}/assistant/generate`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ prompt: currentPrompt }),
          credentials: "include",
        });

        if (!response.ok) {
          let errorText = await response.text();
          console.error("Error response:", errorText);
          throw new Error(
            `Server responded with ${response.status}: ${response.statusText}`
          );
        }

        const data = await response.json();

        // Add assistant response to chat history
        this.addMessageToChat("assistant", data.response);
      } catch (error) {
        console.error("Error generating code:", error);
        this.error =
          error.message ||
          "Failed to generate code. Please check if the backend is running.";
      } finally {
        this.isLoading = false;
        this.scrollToBottom();
      }
    },

    addMessageToChat(sender, content) {
      this.chatHistory.push({
        sender,
        content,
        timestamp: new Date(),
      });
      // Schedule scroll to bottom for after DOM update
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },

    scrollToBottom() {
      if (this.$refs.chatHistory) {
        this.$refs.chatHistory.scrollTop = this.$refs.chatHistory.scrollHeight;
      }
    },

    resetError() {
      this.error = null;
    },

    clearChat() {
      if (confirm("Are you sure you want to clear the entire chat history?")) {
        this.chatHistory = [];
        this.error = null;
      }
    },

    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    },

    copyResponse(message) {
      navigator.clipboard
        .writeText(message.content)
        .then(() => {
          alert("Response copied to clipboard!");
        })
        .catch((err) => {
          console.error("Failed to copy text: ", err);
          alert("Failed to copy to clipboard. Please copy manually.");
        });
    },
  },
  mounted() {
    // Focus the input field on component mount
    this.$nextTick(() => {
      const textareaElement = this.$el.querySelector(".prompt-input");
      if (textareaElement) {
        textareaElement.focus();
      }
    });
  },
};
</script>

<style scoped>
.smart-code-assistant {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
}

/* Header section styles */
.header-section {
  flex-shrink: 0;
  padding: 20px 20px 0;
  background-color: #ffffff;
  border-bottom: 1px solid #e0e0e0;
  z-index: 10;
}

.main-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
  position: relative;
}

h1 {
  color: #2c3e50;
  font-size: 2.5em;
  margin-bottom: 15px;
}

h2 {
  color: #42b983;
  margin-top: 10px;
  margin-bottom: 10px;
}

.description {
  margin-bottom: 20px;
}

/* Chat container styles */
.chat-container {
  flex-grow: 1;
  overflow-y: auto;
  padding: 0 20px;
  background-color: #f8f9fa;
}

/* Chat history styling */
.chat-history {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px 0;
  overflow-anchor: none;
}

.chat-message {
  padding: 10px 15px;
  border-radius: 8px;
  max-width: 85%;
  word-wrap: break-word;
}

.chat-message.user {
  align-self: flex-end;
  background-color: #dcf8c6;
  border: 1px solid #c5e1a5;
}

.chat-message.assistant {
  align-self: flex-start;
  background-color: #f1f0f0;
  border: 1px solid #e0e0e0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 0.8em;
  color: #666;
}

.message-content {
  text-align: left;
}

.message-content pre {
  white-space: pre-wrap;
  font-family: monospace;
  margin: 0;
  padding: 0;
  font-size: 0.9em;
  overflow-x: auto;
}

.message-time {
  font-size: 0.8em;
  color: #999;
}

/* Input section styles */
.input-section {
  flex-shrink: 0;
  padding: 15px 20px;
  background-color: #ffffff;
  border-top: 1px solid #e0e0e0;
  z-index: 10;
}

.prompt-input {
  width: 100%;
  padding: 12px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
  font-size: 14px;
  resize: vertical;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.action-button {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 12px 24px;
  font-size: 1em;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button:hover:not(:disabled) {
  background-color: #3aa876;
}

.action-button:disabled {
  background-color: #a0cfbe;
  cursor: not-allowed;
}

.reset-button {
  background-color: #f0f0f0;
  color: #2c3e50;
  border: 1px solid #ddd;
  padding: 8px 16px;
  font-size: 0.9em;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.reset-button:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.reset-button:disabled {
  color: #aaa;
  background-color: #f8f8f8;
  cursor: not-allowed;
}

.input-help {
  text-align: right;
  font-size: 0.8em;
  color: #999;
  margin-top: 5px;
}

.error-message {
  color: #d9534f;
  background-color: #f9f2f2;
  border: 1px solid #ebccd1;
  border-radius: 4px;
  padding: 15px;
  margin: 10px 0;
  text-align: left;
}

.loading-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px 0;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #42b983;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 2s linear infinite;
  margin: 10px auto;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Mobile Responsive Styles */
@media (max-width: 600px) {
  .header-section {
    padding: 10px 10px 0;
  }

  .chat-container,
  .input-section {
    padding: 10px;
  }

  .chat-message {
    max-width: 90%;
  }

  h1 {
    font-size: 1.8em;
    margin-bottom: 10px;
  }

  .action-button,
  .reset-button {
    font-size: 0.9em;
    padding: 10px 16px;
  }
}
</style>
